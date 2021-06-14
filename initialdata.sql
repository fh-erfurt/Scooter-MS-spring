INSERT INTO area(name, ndegree1, ndegree2, edegree1, edegree2) VALUES ('Erfurt', 50.95, 51.01, 11.00, 11.06);

INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (99, 'HFN354', 'ready', 1, '50.974746', '11.0353385');
INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (10, 'LWS957', 'lowonbattery', 1, '50.973841', '11.031959');
INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (34, 'BFH278', 'damaged', 1, '50.970444', '11.038773');
INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (65, 'PWH463', 'maintenance', 1, '50.973562', '11.052786');
INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (58, 'LDN193', 'ready', 1, '50.987815', '11.027038');
INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (37, 'QRT573', 'ready', 1, '50.990121', '11.012779');
INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (68, 'NDK946', 'ready', 1, '50.986996', '11.003527');
INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (15, 'DKM538', 'ready', 1, '50.974325', '11.014536');
INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (5, 'DKN947', 'lowonbattery', 1, '50.970868', '11.021255');
INSERT INTO scooter(battery, licenseplate, status, area_id, ndegree, edegree) VALUES (2, 'KML486', 'lowonbattery', 1, '50.958063', '11.034833');

INSERT INTO scooterhotspot(name, maxscootercount, scootercount, ndegree, edegree, area_id) VALUES ('Hanseplatz', 10, 0, 50.983801, 11.044834, 1);

INSERT INTO maintenancedepartment(name, maxscootercapacity, scootercapacity, ndegree, edegree, area_id) VALUES ('Leipziger Straße', 5, 0, 50.994634, 11.042946, 1);

INSERT INTO user(email, password, is_admin, is_scooter_hunter, credited_euros) VALUES ('testmail1@gmail.com', '$2a$10$e./R50Vasc2.2TL2B6NMS.2lZ5qHLTj/eMhPJPpKpN14BOFuWyvz2', false, false, '10.39');
INSERT INTO user(email, password, is_admin, is_scooter_hunter, credited_euros) VALUES ('testmail2@gmail.com', '$2a$10$e./R50Vasc2.2TL2B6NMS.2lZ5qHLTj/eMhPJPpKpN14BOFuWyvz2', false, false, '5.12');
INSERT INTO user(email, password, is_admin, is_scooter_hunter, credited_euros) VALUES ('testmail3@gmail.com', '$2a$10$e./R50Vasc2.2TL2B6NMS.2lZ5qHLTj/eMhPJPpKpN14BOFuWyvz2', false, false, '0.24');
