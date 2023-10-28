## Features

- **Single Default Project**: All tasks are initially associated with one default project.
- **Minimal User Table**: Comes with four preset users (John, Paul, George, Ringo).
- **Task Properties**: Each task includes State, AssignedTo, Description, and Comments fields.

<!-- 
  HTTP Requests Section: Discuss the API functionalities available
-->
## HTTP Requests

- **Create Task**: Initializes a new task with State=Todo. Returns a TaskID.
- **Modify Task**: Allows you to update task properties. Returns a success or failure message.
- **Delete Task**: Removes a task from the system. Returns a success or failure message.
- **Show Board**: Provides a list of all tasks in JSON format.
