# Sprint 6 ( 01/04/2026 - 15/04/2026 )

---

## Sprint Goal

---

The goal of this sprint is to implement database localization in the ClassIQ system.
This means storing data in multiple languages (English, Sinhala, Arabic) in the database and showing the correct language in the UI based on the selected language.

## Selected Product Backlog Items

---

- Modify database to support multiple languages
- Store multilingual data (EN, Sinhala, Arabic)
- Retrieve data based on selected language
- Integrate database with UI localization
- Complete remaining UI buttons
- Test database localization
- Update README and sprint documents
- Complete localization Excel sheet


## Planned Tasks/Breakdown

---

- Analyze current database structure
    - Check existing tables and fields

- Update database for localization
    - Add fields like name_en, name_si, name_ar

- Update model classes
    - Modify Student class to support multilingual data

- Update DAO layer
    - Modify queries to fetch data based on language

- Implement language-based data retrieval
    -  Use selected language to get correct data

- Integrate database with UI
    - Show correct language data in UI pages

- Update UI pages
    - Display multilingual data
    - Complete all buttons

- Insert and update multilingual data
    - Add English, Sinhala, and Arabic values
    - 
- Testing
    - Test database storage
    - Test data retrieval
    - Verify UI shows correct language
  
- Complete Excel sheet
    - Add all UI strings and translations
  
- Update documentation
    - Update README with database localization details
    - 
- Prepare sprint report
    - Include team contributions and summary

## Team Capacity & Assumptions

---

- All team members are available during the sprint
- Database is already connected
- UI localization is already completed in Sprint 5
- Team has basic knowledge of database and localization
- No major issues expected

## Definition of Done

___

The sprint is complete when the database supports multilingual data (English, Sinhala, Arabic)
and the system shows the correct language based on user selection. All required classes are updated,
UI displays correct data, and all buttons are working. The system is tested properly. The Excel sheet,
README, and sprint report are completed and updated.