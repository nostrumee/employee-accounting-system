DELETE FROM employee;

INSERT INTO employee (email, password, first_name, last_name, salary, birthday, role)
VALUES
    ('abcdefu@gmail.com',
     '$argon2i$v=19$m=65536,t=2,p=1$S+LxH0tfuznrau5o/zNp8g$SVowWpUqbZl6PkmXmi1qBEN5cvsMcocV8SSnXv+SHTQ',
     'Johnny',
     'Kellerman',
     1234.5,
     '2000-04-24',
     'USER'),
    ('abcdefu123@gmail.com',
     '$argon2i$v=19$m=65536,t=2,p=1$R5EaZY4/ILE33vD2IR9lYA$cs18Jw4hX/CMV4DFWIn+2vKaSZI5a2cmbzW7GvixtW8',
     'John',
     'Doe',
     12345.6,
     '1999-04-24',
     'ADMIN');