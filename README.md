COMP6461-Assignment2 -  Run Instructions

httpfs
httpfs -v -p 6876 -d ./resources/
httpfs -p 6876 -d ./resources/

httpc

File Directory Test
httpc get -l 'http://localhost:6876/get/'

File Test with headers
httpc get -h "Content-Type: application/json~Content-Disposition: inline" -l 'http://localhost:6876/get/'
httpc get -h "Content-Type: text/plain~Content-Disposition: attachment; filename=^6461.txt^" -l 'http://localhost:6876/get/'
httpc get -h "Content-Type: application/json~Content-Disposition: attachment; filename=^6461.txt^" -l 'http://localhost:6876/get/dev'

File Not Found Test
httpc get -l 'http://localhost:6876/get/foos'

Post Test
httpc post -l --d {"CN_ASSIGNMENT": 2} 'http://localhost:6876/post/6461'

Headers
"Content-Type: application/json"
"Content-Type: text/plain"

"Content-Disposition: attachment; filename=\"disposition.txt\""
"Content-Disposition: inline"


