# Hospital Management System Using JavaFx
The purpose of this project is to implement a data management system of a hospital. This system provides the user with the ability to manage information about doctors, patients, their illnesses, their appointments, but also the treatment per appointment.

Below are some screenshots of the application's user interface.
![image](https://user-images.githubusercontent.com/91207835/203411488-5b62cad5-f16d-46b3-8cd3-ee2ff970b8c6.png)
![image](https://user-images.githubusercontent.com/91207835/203411632-5da0608e-f0f5-425d-8aed-790d0fca924d.png)
![image](https://user-images.githubusercontent.com/91207835/203411646-7ff068e9-52c2-4ecb-a60a-bb3ae21a5b5c.png)
![image](https://user-images.githubusercontent.com/91207835/203411668-17c4c00c-a9a6-418b-b2d5-1b16061c5490.png)

**Good to know**

For the proper functioning of the system it is necessary to initially insert the diseases in which the doctors of the hospital specialize, since, when adding a doctor to the system, only one of the categories contained in the disease database can be selected as his specialty. With regard to patients and their appointments, the patient should first be added to the system and then an attempt should be made to register a new booking because, similarly to doctors and diseases, only patients who are in the hospital database are available for selection in the booking registration environment. After the user is sure that the above are met, he can perform any available action he wishes without unwanted errors.

**Known Issues**

Due to the use of Plain text files rather than an integrated database management system, various problems occurred. The problem that needs to be mentioned is the matching of the categories of diseases with the specialties of doctors when adding a patient or scheduling an appointment. More specifically, the expected result was the emergence of the appropriate disease depending on the doctor selected in the environments of registering a new appointment and adding a new patient. This was achieved by creating three different lists, one for each disease category, and by storing, after screening, each of the diseases on the appropriate lists. Then, through a method, a check is carried out for the name of the doctor selected in the field of doctors and the diseases corresponding to the field of diseases are determined. The result of the above way is to correctly match doctors with their specialties but in combination with the use of Plain text files it limits the application in operation with the already existing doctors and their specialties as when adding a disease whose category is new to the system or when adding a new doctor there is no automatic matching of the new category of diseases with a doctor and vice versa after the control in the application code is done statically and not dynamically.


*The tool used to develop the system is JetBrains' Intellij IDEA and the Java version is 14.*
