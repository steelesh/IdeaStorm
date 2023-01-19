<!--- HTML used here to center IdeaStorm text & logo in center of design document -->

  <p align="center">
    <img src="https://user-images.githubusercontent.com/94927484/213072389-f45e5f0e-fa0b-4693-9677-9445514e9ef6.svg" width="65px"/>
  </p>
  
  <h1 align="center">IdeaStorm</h1>

<!--- Markdown Starts here -->

## Design Document

Alex Robinson, Logan Conley, Matthew Thompson, Steele Shreve

---

## Introduction

Are you tired of feeling bored and not knowing what to do with your free time? Want to make the most of every moment and try new things? With IdeaStorm, a fulfilling experience awaits you.

**IdeaStorm** allows you to:
- Generate an activity that tailors to your preferences.
- Create an account to save those activities to a list.
- Reference your list of activities and check them off accordingly.
- Add your tasks to your calendar application.

Use your Android device to cure your boredom, seek new interests and spark your creativity. Works great for individual or group use!

## Storyboard

[IdeaStorm Storyboard](https://www.figma.com/proto/ofwshkq6kyyIyOkC2r7yCo/IdeaStorm?kind=&node-id=1%3A2&page-id=0%3A1&scaling=scale-down&show-proto-sidebar=1&starting-point-node-id=1%3A2&viewport=-471%2C197%2C0.4)

![mockrocket-capture(1)](https://user-images.githubusercontent.com/94927484/213271542-f7dcba0d-e30c-4ab1-ac67-21c967099906.png)

## Functional Requirements


### Requirement 1.0: Generate correct activity data.

#### Scenario

A user wants to find an activity that meets their criteria: 3 participants, social activity, and low price.

#### Dependencies

Activity data is available and accessible.

#### Assumptions

The activity returned is readable.

The activity returned is a group activity.

#### Examples

1.1

**Given** A feed of activity data is available.

**When**  The user specifies the _participants_ field to be 3 instead of the default 1.

**Then** The generator should return a random activity that is applicable for three persons.

1.2

**Given** A feed of activity data is available.

**When** The user specifies the _type_ field to be "social."

**Then** The generator should return a random activity of type "social."

1.3

**Given** A feed of activity data is available.

**When** The user specifies the _price_ field to be "low."

**Then** The generator should return a random activity that has a price of "low."

1.4

**Given** The user wants to change their mind and wants to clear the activity filters.

**When** The user clicks the "Clear filters" button.

**Then** A popup tells user the filters have been cleared successfully. The fields are cleared back to defaults and if there was a generated activity displayed it is not anymore.


### Requirement 2.0: Save activity to correct list(s).

#### Scenario

A user wants to save an activity that was generated and made certain it can be accessed again.

#### Dependencies

Correct activity data is available and accessible.

The user is logged into an account - _firebase auth_.

#### Assumptions

The activity saved is the correct activity.

The correct list contains the desired activity.

The user can access the list after exiting the app and opening it again.

#### Examples

2.1  

**Given** The user's generated activity is "go to a local thrift shop" and user wants to save that activity to their "To-Do List."

**When**  The user clicks the "Add to To-Do List" button.

**Then** A popup tells user that that the save is successful. User can then navigate to their account and see the "go to a local thrift shop" activity included in their "To-Do List."

2.2

**Given** The user's generated activity is "volunteer at a local animal shelter" and user wants to save that activity for later.

**When** The user clicks the button to save it to their "Saved for Later List."

**Then** A popup tells user that the save is successful. User can then navigate to their account and see the "volunteer at a local animal shelter" activity included in their "Saved for Later List."

2.3

**Given** The user resets their Android device (closing the IdeaStorm app); The user wants to open the app and see the activity "learn and play a new card game" remained in their "To-Do List."

**When** The user opens the app again and navigates to the _account_ tab.

**Then** The "To-Do List" remained the same with the activity "learn and play a new card game" saved and included.

2.4

**Given** The user has the activity "write a short story" included in their "Saved for Later List."

**When** The user clicks the "Add to To-Do List" button.

**Then** The activity "write a short story" is not just removed from the "Saved for Later List", but also added to the "To-Do List" now.

## Class Diagram

![IdeaStorm UML Class Diagram](https://user-images.githubusercontent.com/94927484/213340243-43abfcf1-5fe2-4d81-a3ba-f49488df16e3.png)

## Class Diagram Description

**MainActivity:**  The first screen the user sees.  This will have filters and a button to generate an activity.

**AccountDetailsActivity:**  A screen that shows user account details and their lists.

**RetrofitClientInstance:** Bootstrap class required for Retrofit.

**Activity:** Noun class that represents an activity.

**IActivityDAO:** Interface for Retrofit to find and parse Activity JSON.

## Scrum Roles

- DevOps/Product Owner/Scrum Master:
- Frontend Developer:
- Integration Developer:

## Weekly Meeting

Meeting Information
