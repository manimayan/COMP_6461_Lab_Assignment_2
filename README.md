## About The Project

### COMP6461 – Fall 2021 - Data Communications & Computer Networks - Lab Assignment #2

### Introduction 
In this assignment, a simple HTTP server application has been implemented and used it with off-the-shelf HTTP clients (including httpc client, the result of Assignment #1). Precisely, we aim to build a simple remote file manager on top of the HTTP protocol in the server side. 

### Outline 
The following is a summary of the main tasks of the Assignment: 
1. Study-Review HTTP network protocol specifications (Server Side). 
2. Build your HTTP server library that implements the basic specifications. 
3. Develop a minimal file server on top of the HTTP server library. 
4. (optional) Enhance the file server application to support simultaneous multi-requests. 
5. (optional) Implement the support for Content-Type and Content-Disposition headers.  

### httpfs
 ```sh
httpfs -v -p 6876 -d ./resources/
```
```sh
httpfs -p 6876 -d ./resources/
```
### httpc
#### File Directory Test
```sh
httpc get -l 'http://localhost:6876/get/'
```
#### File Test with headers
```sh
httpc get -h "Content-Type: application/json~Content-Disposition: inline" -l 'http://localhost:6876/get/'
```
```sh
httpc get -h "Content-Type: text/plain~Content-Disposition: attachment; filename=^6461.txt^" -l 'http://localhost:6876/get/'
```
```sh
httpc get -h "Content-Type: application/json~Content-Disposition: attachment; filename=^6461.txt^" -l 'http://localhost:6876/get/dev'
```
#### File Not Found Test
```sh
 httpc get -l 'http://localhost:6876/get/foos'
```
#### Post Test
```sh 
httpc post -l --d {"CN_ASSIGNMENT": 2} 'http://localhost:6876/post/6461'
```
##### Headers
```sh
"Content-Type: application/json"
"Content-Type: text/plain"
"Content-Disposition: attachment; filename=\"disposition.txt\""
"Content-Disposition: inline"
```
