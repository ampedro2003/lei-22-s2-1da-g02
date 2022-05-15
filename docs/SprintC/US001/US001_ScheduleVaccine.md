# US 001 - Schedule a Vaccine

## 1. Requirements Engineering

### 1.1. User Story Description

As an **SNS User**, I intend to use the application to **schedule a vaccine**.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

> To take a vaccine, the SNS user should use the application to schedule his/her vaccination.
> 
>The user
should introduce his/her SNS user number, select the vaccination center, the date, and the time (s)he
wants to be vaccinated as well as the type of vaccine to be administered (by default, the system
suggests the one related to the ongoing outbreak). 
>
> Then, the application should check the
vaccination center capacity for that day/time and, if possible, confirm that the vaccination is
scheduled and inform the user that (s)he should be at the selected vaccination center at the
scheduled day and time.
>
> The SNS user may also authorize the DGS to send an SMS message with
information about the scheduled appointment. If the user authorizes the sending of the SMS, the
application should send an SMS message when the vaccination event is scheduled and registered in
the system.



**From the client clarifications:**

> **Question:** "What kind of information should be included in an SMS Message to warn of a scheduling? (for example, the SNS number, center, day, time and vaccine type)?"
>
> **Answer:** Date, Time and vaccination center.

> **Question:** "...The SNS user may also authorize the DGS to send an SMS message with information about the scheduled appointment..."
>
>Which interpretation is correct?: The user should and will authorize or the user can do so if he wants. Because if he doesn't authorize how will he receive the information?
>
> 
>
> **Answer:** The SNS user should give authorization to DGS so that DGS can send him a SMS.
>
>When scheduling a vaccination event, the SNS user should always see all the information about the scheduled appointment.





### 1.3. Acceptance Criteria

* **AC1:** A SNS user cannot schedule the same vaccine more than
  once.
* **AC2:** All required data must be filled.
* **AC3:** The vaccination center has to have availability.

### 1.4. Found out Dependencies

There is a dependency related to the US003 and US014, since for an SNS User to schedule a vaccine there's the need of having him registered in the System.
There is a dependency to the US009 and US013, since in order to schedule a vaccine it is required that the System has Vaccination Centers and Vaccines, respectively.


### 1.5 Input and Output Data

**Input Data:**
* Typed data:
  - SNS Number

* Selected data:
    - Vaccination Center;
    - Vaccine Type;
    - Date;
    - Time;
    

**Output Data:**

* A list with all the Vaccination Centers available
* A list with all the Vaccine Types available
* A list with dates
* A list with times
* (In)Success of the operation

### 1.6. System Sequence Diagram (SSD)

![US001_SSD](US001_SSD.svg)

### 1.7 Other Relevant Remarks

No other relevant remarks.

## 2. OO Analysis

### 2.1. Relevant Domain Model Excerpt

![US001_DM](US001_DM.svg)

### 2.2. Other Remarks

No other relevant remarks.

## 3. Design - User Story Realization 

### 3.1. Rationale

**The rationale grounds on the SSD interactions and the identified input/output data.**

| Interaction ID | Question: Which class is responsible for... | Answer  | Justification (with patterns)  |
|:-------------  |:--------------------- |:------------|:---------------------------- |
| Step 1         |    ... interacting with the actor? | ScheduleVaccineUI   |  Pure Fabrication: there is no reason to assign this responsibility to any existing class in the Domain Model.   |
| 			  		 |    ... coordinating the US? | ScheduleVaccineController | **Controller**  |
| Step 2  |    ...transfer the data typed in the UI to the domain? | ScheduleVaccineDTO | **DTO:** When there is so much data to transfer, it is better to opt by using a DTO in order to reduce coupling between UI and domain |
| Step 3     |    ... instantiating a new Vaccine  | Company | By applying the **Creator** pattern, the "Company" is responsible for instantiating a new "Vaccine", since it is the one who storages the Vaccines.   |
| 	 |    ... instantiating a new Administration Process | Vaccine |By applying the **Creator** pattern, the "Vaccine" is responsible for instantiating the "Administration Process", since a "Vaccine" has/contains an Administration Process|
| Step 4         |    ...validating the inputted data for the Vaccine | Vaccine | The Vaccine class should know what needs to be validated in order to actually create a "Vaccine"|
| 		 |    ...validating the inputted data for the Administration Process | Administration Process |The Administration Process class should know what needs to be validated in order to actually create an "Administration Process"  |
| Step 5         |    ...saving the inputted data for the Vaccine ? | Vaccine | **IE:** A Vaccine has its own data |
|   		 |    ...saving the inputted data for the Administration Process? | AdministrationProcess  | **IE:** An Administration Process has its own data |
| Step 7  |    ... informing operation success | SpecifyVaccineAndAdminProcessUI  | **IE:** is responsible for user interactions  | 

### Systematization ##

According to the taken rationale, the conceptual classes promoted to software classes are: 

* VaccineAndAdminProcessDto
* Vaccine
* AdministrationProcess
* Company (already implemented)

Other software classes (i.e. Pure Fabrication) identified: 
* SpecifyVaccineAndAdminProcessUI
* SpecifyVaccineAndAdminProcessController

## 3.2. Sequence Diagram (SD)

![US013-SD](US013_SD.svg)

## 3.3. Class Diagram (CD)

*In this section, it is suggested to present an UML static view representing the main domain related software classes that are involved in fulfilling the requirement as well as and their relations, attributes and methods.*

![US013_CD](US013_CD.svg)
