# Sprint 5 ( 18/03/2026 - 01/04/2026 )

---

## Sprint Goal

---

The objectives to be achieved in this sprint include preparing the ClassIQ application 
to support multilingual functionality by localizing the user interface, externalizing all 
the user interface text, incorporating a localization framework, adding support for non-Latin 
languages like the Arabic language, and making the application scalable to add support for any 
other language in the future.

## Selected Product Backlog Items

---

- Externalize all UI text
- Implement language selector
- Support Arabic language (RTL)
- Add second non-Latin language which is Sinhalese
- Enable dynamic language switching
- Implement RTL layout adjustments


## Planned Tasks/Breakdown

---

- Identify and extract UI text into translation files
  - Identify all hardcoded text in the UI and move them into separate files.


- Refactor code to load text from translation files


- Create translation files (e.g., en.json, ar.json)
  - Create structured files where text is stored in key-value pairs.


- Integrate localization framework (e.g., i18n)
  - Integrate a library to manage translation and language switching.


- Develop language selector feature
  -  Create a dropdown or menu for users to select their preferred language.


- Add Arabic translation (RTL support)


- Add second language translation (Sinhalese)


- Implement dynamic language switching
    - Implement functionality to change languages instantly.


- Adjust UI layout for RTL languages
  - Implement changes to the layout to support RTL languages.


- Update README with localization instructions
  - Document how to use localization in the project.


- Update backlog with acceptance criteria
  - Update the backlog with localization-related tasks.

## Team Capacity & Assumptions

---

- All team members are available throughout the sprint
- All team members contribute equally to the sprint
- Basic knowledge of localization tools is available
- No major blockers expected while implementing


## Definition of Done


___


The sprint 5 tasks are considered complete when all the UI text is externalized to translation 
files. Also, a functional language selector is included to facilitate easy switching between 
languages. The application supports at least two non-Latin scripts. The UI dynamically 
updates when the language is changed. RTL layout is correctly implemented to support languages
such as Arabic. There is no hardcoded text in the UI. All the localization features are 
correctly documented in the README. Also, the project backlog is updated to include all 
localization-related tasks and requirements.