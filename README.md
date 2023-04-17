# Collaborative Text Editor

This project is a collaborative text editor similar to the one that Google Docs provides. It allows multiple users to edit a document simultaneously and see each other's changes in real-time. The application was developed using Java, Swing, and RabbitMQ.

## Features

- Multiple users can edit the same document at the same time.
- Changes made by one user are immediately visible to all other users in real-time.
- Users can see which part of the document other users are currently editing.
- The application uses RabbitMQ for message queuing to ensure asynchronous communication between users. ( we send real time positions of users on the file, notifications about insertion, deletion positions and letting others write )
- Two versions of the application are provided: one with full lock, where only one user can edit the document at a time, and another with loose lock, where multiple users can edit different parts of the document simultaneously.

## How to Use

To use the application, simply run the `Main` class. Upon starting the application, the user will be given an unique ID. Once a username is entered, the user will be taken to the main screen where a document is created. Happy collaborative editing! 

## Videos

- [Full Lock Version](https://youtu.be/hc9LjIKNy-8)
- [Loose Lock Version](https://youtu.be/61s1PzhDoZ0)

## Future Work 
- Detect deadlock and give the right to write for a random user that requested it.
- Implement more advanced features such as user authentication and file versioning.
- Improve the user interface to make it more intuitive and user-friendly.
- Optimize the performance of the application to support larger documents and more users.
