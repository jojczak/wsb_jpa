
INSERT INTO DOCTOR (first_name, last_name, telephone_number, email, doctor_number, specialization)
VALUES
    ('Jan', 'Kowalski', '123456789', 'jan.kowalski@example.com', 'DOC123', 'GP'),
    ('Anna', 'Nowak', '987654321', 'anna.nowak@example.com', 'DOC456', 'DERMATOLOGIST'),
    ('Tomasz', 'Lis', '456789123', 'tomasz.lis@example.com', 'DOC789', 'SURGEON'),
    ('Karolina', 'Mazur', '321654987', 'karolina.mazur@example.com', 'DOC321', 'OCULIST');


INSERT INTO PATIENT (first_name, last_name, telephone_number, email, patient_number, date_of_birth)
VALUES
    ('Piotr', 'Wiśniewski', '111222333', 'piotr.wisniewski@example.com', 'PAT001', '1985-06-15'),
    ('Marta', 'Zielińska', '444555666', 'marta.zielinska@example.com', 'PAT002', '1992-11-23'),
    ('Kamil', 'Nowicki', '555777888', 'kamil.nowicki@example.com', 'PAT003', '2001-09-05'),
    ('Alicja', 'Jankowska', '666999000', 'alicja.jankowska@example.com', 'PAT004', '1978-04-17'),
    ('Robert', 'Lewandowski', '777888999', 'robert.lewandowski@example.com', 'PAT005', '1995-01-30');


INSERT INTO ADDRESS (city, address_line1, address_line2, postal_code, doctor_id, patient_id)
VALUES
    ('Warszawa', 'ul. Długa 5', 'Apt 12', '00-123', 1, NULL),
    ('Kraków', 'ul. Krótka 8', NULL, '30-456', NULL, 2),
    ('Gdańsk', 'ul. Morska 25', 'Lokal 7', '80-987', 3, NULL),
    ('Wrocław', 'ul. Mostowa 3', NULL, '50-321', NULL, 4),
    ('Poznań', 'ul. Zielona 10', NULL, '60-001', NULL, 5);


INSERT INTO VISIT (description, time, doctor_id, patient_id)
VALUES
    ('Kontrola ogólna', '2025-04-01T10:00:00', 1, 1),
    ('Badanie dermatologiczne', '2025-04-02T15:30:00', 2, 2),
    ('Operacja kolana', '2025-04-03T09:00:00', 3, 3),
    ('Badanie wzroku', '2025-04-04T12:15:00', 4, 4),
    ('Kontrola po operacji', '2025-04-10T14:00:00', 3, 1),
    ('Badanie serca', '2025-04-11T16:45:00', 1, 5);


INSERT INTO MEDICAL_TREATMENT (description, type, visit_id)
VALUES
    ('Wykonano EKG', 'EKG', 1),
    ('Zbadano skórę pod kątem alergii', 'USG', 2),
    ('Wykonano rekonstrukcję ACL', 'RTG', 3),
    ('Dopasowano nowe okulary', 'USG', 4),
    ('Rehabilitacja po operacji', 'EKG', 5),
    ('Wykonano kompleksowe USG serca', 'USG', 6);
