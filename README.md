

Changes

- Return 404 instead of 413 if the index is too large. From w3, a 413 response code means "The server is refusing to process a request because the request entity is larger than the server is willing or able to process. The server MAY close the connection to prevent the client from continuing the request". Instead, we should return a 404, page not found because that resource doesn't exist

- Add end points to list all lines, create a new line, update an exisitng line or delete a line. modifications would update file appropriately

- Reload data if file changes

- stop using a file for the source. instead store more perisstently in a database

- preload data into cache before app starts
