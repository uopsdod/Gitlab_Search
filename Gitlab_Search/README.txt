### Resources overall: 

Project_Git_Url

Lambda
	Role: AWSLambdaBasicExecutionRole
		Policy: AWSLambdaBasicExecutionRole
		Policy: [aws_policy_ssm.txt]

### Invoking Flow: 
Gatway API -> Lambda(Parameter Store + CloudWatch log) 

### TODO: 
* use Jersey to create a simple Restful API for client
* upload to lambda and test 
* use Gateway API + lambda to make it serverless 
