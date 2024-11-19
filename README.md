# ScanMate Mock-Backend Version - Web Programming Course

## Grading & Collaboration

This project combines two courses: Web Programming (frontend) and Core Concepts of Software Engineering (backend), with grading limited to each course's relevant portion. Both instructors have approved this. Marven and Lars developed the project collaboratively, and it should be evaluated as a group.

## Who did what?

Marven and Lars worked together on all code through pair programming, even if GitHub commits appear uneven, contributing equally to design and problem-solving.

## Whats is the project about?

The project is a supermarket checkout system, serving as a proof of concept and not intended for real-world use.

<br> <br> <br>

# Detailed mock-backend using JSON Server Setup Guide 

This guide provides step-by-step instructions for setting up and running the mock-backend server for the ScanMate project. Please follow these steps carefully to ensure all components are installed correctly and the server runs smoothly.

This Guide is primarily for macOS, as we have noticed that you used a Mac in the lectures.

## 0. Install Homebrew (if not already installed)

Homebrew is a package manager for macOS that will make it easier to install dependencies like NodeJS and npm.

- If Homebrew is not already installed on your system, follow these steps:
  - Visit the [Homebrew website](https://brew.sh) for more information.
  - Run the following command in your terminal to install Homebrew:
    ```bash
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    ```
  - After installation, verify that Homebrew is installed by running:
    ```bash
    brew --version
    ```
  - If the version appears, Homebrew has been installed successfully.
    ```bash
    Output:
    Homebrew 4.4.6
    ```

## 1. Install NodeJS & npm 

The mock-backend of this project requires NodeJS and npm.

- **NodeJS**:

  - Use Homebrew to install NodeJS by running:
    ```bash
    brew install node
    ```
  - Verify the NodeJS version:
    ```bash
    node --version
    ```
    Expected output:
    ```
    v23.2.0
    ```

- **npm**:
  - Use Homebrew to install npm by running:
    ```bash
    brew install npm
    ```
  - Verify the npm version:
    ```bash
    npm --version
    ```
    Expected output:
    ```
    10.9.0
    ```

## 2. Install JSON Server & Express

The mock-backend uses JSON Server and Express.

- Use npm to install JSON Server by running:
  ```bash
  npm install -g json-server
  ```
- Use npm to install Express by running:
  ```bash
  npm install express
  ```

## 3. Download the Project Repository

- Go to the GitHub repository and **use the "json-server-version" branch**: [SelfCheckout-CoSE-WebProg](https://github.com/lzieger03/SelfCheckout-CoSE-WebProg).
- Click on the "Code" button and select "Download ZIP".
- Extract the ZIP file to a location of your choice.

## 4. Navigate to the mock-backend Directory

- Open a terminal (macOS) or Command Prompt (Windows).
- Navigate to the `mock-backend/` folder of the extracted project directory. Use the `cd` command:
  ```bash
  cd path/to/extracted-folder/location/mock-backend
  ```
- Run the following command to start the backend server:
  ```bash
  npm start
  ```
  Expected output:
  ```
  > mock-backend@1.0.0 start
  > node server.js

  JSON Server is running on port 3000
  ```

<br>

# Frontend Setup Guide using Live Server (VSCode)

## 6. Install Visual Studio Code (VSCode)

To work with the frontend part of the project, you will need Visual Studio Code and a Live Server Plugin, if you dont have it already, follow these steps:

- **Download and Install VSCode**:

  - Visit the [Visual Studio Code download page](https://code.visualstudio.com/Download).
  - Download and follow the installation instructions for your operating system.

- **Install the "Live Server" Plugin**:
  - Open VSCode and go to the Extensions view by clicking on the Extensions icon in the Activity Bar or pressing `Ctrl+Shift+X`.
  - Search for "Live Server" by `ritwickdey` and click "Install".

## 7. Run the Frontend

1. Open the project folder in VSCode by selecting "File > Open Folder" and navigate to the extracted project directory.
2. Navigate to the `frontend` folder.
3. Right-click on `index.html` and select "Open with Live Server".
4. A browser window will open displaying the ScanMate site. You can now interact with the system by entering barcodes, inputting discount codes, or logging into the admin view.

## Additional Notes

- Ensure that both the mock-backend and frontend are running simultaneously for the full application to work.
- If any errors occur, verify the compatibility of NodeJS and npm versions as specified in the installation steps.

## Troubleshooting

- **NodeJS or npm Command Not Found**: Make sure the installation paths have been correctly added to your system's `PATH` environment variable.
- **Frontend Not Loading Properly**: Confirm that the mock-backend server is running before starting the Live Server.

<br><br><br>

## Item numbers and discount codes and for quick usage or test purposes

### Item numbers

- 8156679408476 (Apple)
- 4012345678901 (Banana)
- 1234567890123 (Cherry)

### Discount codes

- SALE15 for 15% discount
- EXTRA50 for 50% discount
- TEST100 for free shopping

### Admin login

The admin login works but the admin view is **not implemented** in the mock-backend version of the project.

- Username: admin
- Password: password123
