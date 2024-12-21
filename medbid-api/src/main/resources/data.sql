INSERT INTO ROLE(ID, "NAME", DESCRIPTION)
VALUES (NEXTVAL('granted_authorities_id_sequence'),'USER', 'User role with most restrictive access'),
       (NEXTVAL('granted_authorities_id_sequence'),'ADMIN', 'Admin access'),
       (NEXTVAL('granted_authorities_id_sequence'),'SUPER_ADMIN', 'Super admin access'),
       (NEXTVAL('granted_authorities_id_sequence'), 'ORGANIZATION', 'Organization simple role');