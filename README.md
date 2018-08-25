# rest-items sample service

## Deployment

1. `gem install --user cloudformation-tool`
2. `aws --profile cx --region us-east-1 ec2 create-key-pair --key-name rest-items | jq -cr .KeyMaterial > ~/.ssh/rest-items.pem`
3. `cftool create -p KeyName=rest-items deployment/ rest-items`
4. `cftool servers rest-items`
