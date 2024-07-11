#!/usr/bin/ruby

require 'yaml'
require 'json'
require 'net/http'
require 'i18n_yaml_sorter'

def stack_root
	'deployment'
end

def get_releases
  JSON[Net::HTTP.get_response(URI.parse "https://cloud-images.ubuntu.com/locator/ec2/releasesTable").body.gsub(/,\s*\]/,"]")]["aaData"]
end

def get_minimal_releases
	images = {}
	`aws --profile cx --region us-east-2 ec2 describe-images --filters 'Name=name,Values=ubuntu-minimal/images/hvm-ssd*' --query 'sort_by(Images, &CreationDate)' | jq -cr '. | reverse | .[] | select(.ImageOwnerAlias != "aws-marketplace") | .ImageId as $ami | (.Name | split("/") | .[-1] | split("-") | { $ami, name: .[1], release: .[-1] })'`.split("\n").each do |json|
		rel = JSON[json]
		images[rel['release']] = rel['ami'] unless images.has_key? rel['release']
	end
	images
end

File.open("#{stack_root}/ubuntu-images.yaml","w") do |f|
f << I18nYamlSorter::Sorter.new(StringIO.new(({
  "Mappings" => {
    "UbuntuRegionImages" => get_releases.select do |i|
      #i[2] =~ /LTS/ and
      i[3] == "amd64" and i[4] =~ /^hvm:ebs-ssd/
    end.sort do |a,b|
      a[5] <=> b[5]
    end.collect do |i|
      i[6].gsub! /<[^>]+>/, ""
      i[1] = i[1].gsub(/ \w+$/,"").downcase
      i
    end.inject({}) do |m,i|
      (m[i[0]]||={})[i[1]] ||= i[6]
      m
    end
  }
}).to_yaml)).sort
end
