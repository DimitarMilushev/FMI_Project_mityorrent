# A small torrenting application

### Server
A central server that holds torrents metadata.
The user is just the name, attached to an IP+port, similar to what a
DNS would attach.

Requirements:
- Non-blocking IO for multiple clients serving.
- Save torrent data in the working memory.
- To receive server.messages
- To send server.messages (responses)

#### Architecture
Type: **Three tier**
- **Data**
Here we place the repository
- **Business**
Here's the core functionality of our program. A service will
hold a repository instance and do mutations.
- **Controller**
We need a processor to interpret the request and route it to the 
methods.
- **Utility**
Since this is not an HTTP server out of the box, we'll need a token processor
that will read requests similar to an HTTP request. 

Command set: 

- `register <user> <file1, file2, file3, …., fileN>`
- `unregister <user> <file1, file2, file3, …., fileN>`
- `list-files`
- `download <user> <path to file on user> <path to save>`

Metadata is stored inside a .csv file in the following format
`user;ip;port;filePath`

| user     | IP        | port | filePath              |
|----------|-----------|------|-----------------------|
| gosho123 | 127.0.0.1 | 1234 | "D:/bin/filename.txt" |
### Client
A console application with the following commands:
- `register <user> <file1, file2, file3, …., fileN>`
- `unregister <user> <file1, file2, file3, …., fileN>`
- `list-files`
- `download <user> <path to file on user> <path to save>`