---
title: "Installing and configuring a Skyve development environment"
permalink: /appendix-installing-and-configuring/
excerpt: "Installing and configuring a Skyve development environment"
toc: true
sidebar:
  title: "Index"
  nav: docs
---

These instructions describe the process required to install and configure the 
development environment for Skyve. These instructions assume that you are 
using Windows and SQL Server. Some changes will need to be made if using a 
different operating system or database.

## Prerequisites and Overview

Before starting, you'll need to install several components:

- **Eclipse IDE**: A development environment for writing and managing code
- **Java Development Kit (JDK)**: Required to run Java applications
- **Wildfly Application Server**: A server that runs your Skyve application
- **Database**: SQL Server (or another supported database) to store your application data
- **Git**: Version control system to download the Skyve project

This guide will walk you through installing each component step by step.

## Download and Install Eclipse and JDK

### Download and Install Eclipse

For this section, we have chosen to show how to use Eclipse as the development environment, however, if you prefer, you can use other development environment tools such as IntelliJ, NetBeans, Visual Studio Code, etc. However, if you're not completely familiar with these, use Eclipse so that you can follow the steps below.

**Important Note**: Do not use the Eclipse EE9 preview. You will need the Jakarta EE Full & Web Distribution.

1. Go to the [Eclipse Downloads page](https://www.eclipse.org/downloads/packages/)
2. Click on **Eclipse IDE for Enterprise Java Developers** (this includes all the tools needed for Java web development)

   ![Download Eclipse](../assets/images/appendix/installing-configuring/1.jpg "Download Eclipse")

3. Click the **Download** button
4. Run the downloaded installer and follow the installation wizard
5. When prompted, choose a directory to install Eclipse (e.g., `C:\Program Files\Eclipse`)
6. Complete the installation and create a desktop shortcut if offered

### Download and Install JDK

The Java Development Kit (JDK) is required to compile and run Java applications. Skyve requires JDK 17 or 21.

1. Go to [AdoptOpenJDK](https://adoptium.net/temurin/releases/?package=jdk&version=17)
2. Download **OpenJDK 17** (or JDK 21 if you prefer)
3. Run the installer and follow the installation wizard
4. **Important**: Note the installation path (usually `C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot\`)
5. After installation, verify it works by opening a command prompt and typing:
   ```
   java -version
   ```
   You should see version information displayed.

## Install and Configure Wildfly Server

Wildfly is an application server that runs your Skyve application and makes it accessible through a web browser. Think of it as the "engine" that powers your web application. Skyve apps can be deployed on recent versions of Wildfly (27+).

### What is Wildfly?
Wildfly is a Java application server that:
- Runs your Skyve application
- Handles web requests from browsers
- Manages database connections
- Provides security and other enterprise features

### Install Wildfly Server

You can skip this step if WildFly Server is already installed on your system.

### Install JBoss Tools Plugin

Before installing WildFly Server, we need to install the JBoss Tools plugin in Eclipse. This plugin provides tools to manage WildFly servers.

1. In Eclipse, click **Help** → **Eclipse Marketplace**

   ![JBoss tools](../assets/images/appendix/installing-configuring/2.JPG "JBoss tools")

2. In the search bar, type `jboss` and press Enter

3. Find **JBoss Tools** in the search results and click **Install**

   ![JBoss install](../assets/images/appendix/installing-configuring/3.JPG "JBoss install")

4. In the installation dialog, select all the components as shown below

   ![JBoss install selection](../assets/images/appendix/installing-configuring/15.jpg "JBoss install selection")

5. Click **Next** and follow the installation wizard
6. Accept the license agreement and click **Finish**
7. Restart Eclipse when prompted

### Verify JBoss Tools Installation

To confirm JBoss Tools is installed correctly:

1. Click **Help** → **Eclipse Marketplace**

   ![JBoss tool install](../assets/images/appendix/installing-configuring/4.JPG "JBoss tool install")

2. Click the **Installed** tab
3. Look for **JBoss Tools** in the list - if it's there, you're ready to continue

   ![JBoss tool plugin](../assets/images/appendix/installing-configuring/5.JPG "JBoss tool plugin")

### Install WildFly Server

Now we'll create a WildFly server instance in Eclipse:

1. In Eclipse, right-click in the **Project Explorer** panel (the left sidebar) and select **New** → **Other**

   ![new-other](../assets/images/wildfly/1.png "new-other")
   
2. In the "Select a wizard" dialog, type `server` in the search box, select **Server**, and click **Next**

   ![server](../assets/images/wildfly/2.png "server")

3. Select **WildFly** from the list (choose version 27 or later) and click **Next**

   ![select server](../assets/images/wildfly/3.png "select server")

4. Click **Next** again

   ![select Next](../assets/images/wildfly/4.png "select Next")

5. Click **Download and install runtime** to download WildFly

   ![install runtime](../assets/images/wildfly/5.png "install runtime")

6. Select the latest version of WildFly and click **Next**

   ![select runtime](../assets/images/wildfly/6.png "select runtime")

7. Read and accept the license agreement by checking the box, then click **Next**

   ![accept agreement](../assets/images/wildfly/7.png "accept agreement")

8. Choose where to install WildFly (e.g., `C:\wildfly`) and select your JDK installation, then click **Finish**

   ![download-runtime](../assets/images/wildfly/8.png "download-runtime")
   ![runtime-jre](../assets/images/wildfly/9.png "runtime-jre")

9. Wait for Eclipse to download and install WildFly (this may take several minutes)

**Note**: You should now see a WildFly server listed in the **Servers** tab at the bottom of Eclipse.

## Configure Eclipse IDE

### Set Up Eclipse Workspace

A workspace is a folder where Eclipse stores all your projects and settings.

1. Create a workspace folder:
   - Navigate to `C:\` in Windows Explorer
   - Right-click and select **New Folder**
   - Name it `workspace`
   - **Important**: Avoid spaces in folder names to prevent Java path issues

2. Start Eclipse and set up the workspace:
   - Launch Eclipse
   - When prompted for workspace location, browse to `C:\workspace\`
   - Check **Use this as the default and do not ask again**
   - Click **Launch**
   - Close the welcome screen if it appears

### Configure Java Compiler

Set Eclipse to use Java 17 (required for Skyve):

1. Go to **Window** → **Preferences**
2. Navigate to **Java** → **Compiler**
3. Set **Compiler compliance level** to **17**
4. Click **Apply and Close**
5. When prompted to rebuild, click **Yes**
  
See additional details in [Setting up a Skyve instance](./../pages/appendix_setting-up-a-skyve-instance)

### Additional Recommended Setup

For a complete development environment, we also recommend:

- **Database**: Install a database like MySQL, PostgreSQL, or SQL Server (see database setup below)
- **Version Control**: Register for GitHub, GitLab, or BitBucket for code management
- **Skyve Community**: Join [Skyve Foundry](https://foundry.skyve.org/foundry) for resources
- **Support**: Join our [public Slack](https://join.slack.com/t/skyveframework/shared_invite/enQtNDMwNTcyNzE0NzI2LTRkMWUxZDBlZmFlMmJkMjQzYWMzYWQxMmQzYWQ1ZTdlODNkNjRlYzVhYjFmMmQ4NTlhYWY4MjNhMGVkZGNlMjY) for help

## Set Up Database (SQL Server Example)

Skyve needs a database to store your application data. This example uses Microsoft SQL Server.

### Install SQL Server

1. Download SQL Server:
   - Go to the [Microsoft SQL Server downloads page](https://www.microsoft.com/en-au/sql-server/sql-server-downloads)
   - Download the **Developer** or **Express** edition (both are free for development use)
   - Run the installer and follow the setup wizard

2. Install SQL Server Management Studio (SSMS):
   - Download from [Microsoft's SSMS page](https://docs.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms)
   - This tool lets you manage your database

### Create Database and User

1. **Connect to SQL Server**:
   - Open SQL Server Management Studio
   - Choose **Windows Authentication** and click **Connect**

2. **Create the database**:
   - Right-click **Databases** in the left panel
   - Select **New Database...**
   - Name it `skyve` and click **OK**

3. **Create a database user**:
   - Right-click **Security** → **Logins** → **New Login...**
   - Enter a login name (e.g., `skyve_user`)
   - Choose **SQL Server authentication**
   - Enter a strong password (write it down!)
   - Uncheck **Enforce password expiration**
   - Uncheck **User must change password at next login**
   - Set **Default database** to `skyve`
   - In **Server Roles**, check **sysadmin**
   - In **User Mapping**, check the `skyve` database
   - Click **OK**

4. **Note the port number**:
   - If you just installed SQL Server, you may need to configure the port
   - See [these instructions](https://community.spiceworks.com/how_to/124598-find-the-port-sql-server-is-using-and-change-a-dynamic-port-to-static) for port configuration
   - Write down the port number (usually 1433)

## Import the Skyve Project

Now we'll download and import the Skyve project into Eclipse.

*Note: These instructions are for Eclipse. If using another IDE, you'll need to find similar import instructions.*

### Step 1: Download the Skyve Project

1. Open a command prompt or terminal
2. Navigate to your workspace directory:
   ```
   cd C:\workspace
   ```
3. Clone the Skyve repository:
   ```
   git clone https://github.com/skyvers/skyve.git
   ```
4. Wait for the download to complete

### Step 2: Import into Eclipse

1. In Eclipse, go to **File** → **Import...**
2. Expand **Maven** and select **Existing Maven Projects**
3. Click **Next**
4. Click **Browse...** and navigate to the `skyve` folder you just downloaded
5. Select the `skyve` folder and click **Select Folder**
6. Eclipse should automatically detect multiple Maven projects (you'll see them listed with checkboxes)
7. Ensure all projects are checked and click **Finish**
8. Wait for Eclipse to import and build all projects (this may take several minutes)

### Step 3: Verify Import

- You should see multiple projects in the Project Explorer (left panel)
- Wait for the progress indicator in the bottom-right corner to finish
- If you see any errors, try **Project** → **Clean** → **Clean all projects** → **OK**

## Start Your Skyve Application

### Development Environment

1. **Start WildFly Server**:
   - In Eclipse, go to the **Servers** tab (bottom panel)
   - Right-click your WildFly server and select **Start**
   - Wait for the server to start (watch the Console tab for messages)

2. **Deploy Your Application**:
   - Right-click your Skyve project in Project Explorer
   - Select **Run As** → **Run on Server**
   - Choose your WildFly server and click **Finish**

3. **Access Your Application**:
   - Open a web browser
   - Go to `http://localhost:8080/your-app-name`
   - You should see the Skyve application login page

**Note**: Skyve creates a default admin user for development. Check your project's configuration files for the default login credentials.

### Production Environments

For UAT and production environments, WildFly should be configured as a Windows service. See the WildFly documentation for detailed instructions.

### Test from Mobile Device (Optional)

You can test your Skyve application from mobile devices on the same network.

#### Step 1: Find Your Computer's IP Address

1. Open Command Prompt (press Windows + R, type `cmd`, press Enter)
2. Type `ipconfig` and press Enter
3. Look for your network adapter's IPv4 address (e.g., 192.168.1.100)

![Local IP Configuration](./../assets/images/appendix/local_ip_config.png "Local IP Configuration")

#### Step 2: Configure WildFly for External Access

1. In Eclipse, double-click your WildFly server in the **Servers** tab
2. In the **Server Locations** section, select **Use custom location**
3. In the **Open launch configuration** link, add `-b 0.0.0.0` to the **Program arguments**
4. Save and restart WildFly

![Setting Wildfly for external access](./../assets/images/appendix/wildfly_external_access.png "Setting Wildfly for external access")

#### Step 3: Update Application Configuration

1. Find your project's configuration file (usually a `.json` file)
2. Update the `server url` setting to use your IP address:
   ```
   http://192.168.1.100:8080/
   ```
3. Keep your existing context path (e.g., `/myapp`)

#### Step 4: Test from Mobile

1. Connect your mobile device to the same network as your computer
2. Open a browser on your mobile device
3. Navigate to: `http://YOUR_IP_ADDRESS:8080/your-app-name`
4. You should see your Skyve application

**[⬆ back to top](#installing-and-configuring-a-skyve-development-environment)**

---
**Next [Setting up a Skyve instance](./../_pages/appendix-setting-up-a-skyve-instance.md)**<br>
**Previous [Deploying a Skyve application](./../_pages/appendix-deploying-a-skyve-application.md)**
