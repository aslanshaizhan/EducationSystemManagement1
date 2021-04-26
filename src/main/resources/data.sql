Insert into users(id , firstname , lastname , login , balance , password, blocked) values
    (nextval('users_id_seq') , 'admin' , 'admin' , 'admin', 0 , '$2y$12$eNW4LjmMs0SjEqGV8c49tOyQ2JALrb5HSVsmbIKoJvix6OMcy5IWK' , false);

Insert into user_roles (user_id , role_id) values ((select id from users where login='admin') , (select id from roles where name='ROLE_ADMIN'));

Insert into roles(id, name) values (nextval('roles_id_seq'), 'ROLE_USER');
Insert into roles(id, name) values (nextval('roles_id_seq'), 'ROLE_ADMIN');


Insert  into education_center(id , name , location , created_at , updated_at) values (nextval('education_center_id_seq') ,
    'GLOBAL_STUDENT' , 'Almaty , street Abay , 16d' , now() , now());
Insert  into education_center(id , name , location , created_at , updated_at) values (nextval('education_center_id_seq') ,
    'EDU_KZ' , 'Almaty , street Lenin , 16d' , now() , now());
Insert  into education_center(id , name , location , created_at , updated_at) values (nextval('education_center_id_seq') ,
    'TEST_CENTER' , 'Astana , street Abay , 16d' , now() , now());
Insert  into education_center(id , name , location , created_at , updated_at) values (nextval('education_center_id_seq') ,
    'LOST_IN_YOU_MY_MIND' , 'Astana , street Nazarvayev , 16d' , now() , now());
Insert  into education_center(id , name , location , created_at , updated_at) values (nextval('education_center_id_seq') ,
    'SCIENCE_FOREVER' , 'Karagandy , street Putin , 112d' , now() , now());