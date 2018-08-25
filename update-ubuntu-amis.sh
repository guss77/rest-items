#!/usr/bin/ruby

require 'yaml'
require 'json'
require 'net/http'

def get_releases
  JSON[Net::HTTP.get_response(URI.parse "https://cloud-images.ubuntu.com/locator/ec2/releasesTable").body.gsub(/,\s*\]/,"]")]["aaData"]
end

File.open("deployment/ubuntu-images.yaml","w") do |f|
f << ({
  "Mappings" => {
    "UbuntuRegionImages" => get_releases.select do |i|
      i[2] =~ /LTS/ and i[3] == "amd64" and i[4] == "hvm:ebs-ssd"
    end.sort do |a,b|
      a[5] <=> b[5]
    end.collect do |i|
      i[6].gsub! /<[^>]+>/, ""
      i
    end.inject({}) do |m,i|
      (m[i[0]]||={})[i[1]] ||= i[6]
      m
    end
  }
}).to_yaml
end
