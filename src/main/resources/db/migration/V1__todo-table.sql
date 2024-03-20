CREATE TABLE todo (
     id uuid NOT NULL,
     created_at timestamp(6) NULL,
     deleted_at timestamp(6) NULL,
     description varchar(255) NULL,
     due_date timestamp(6) NULL,
     title varchar(255) NULL,
     updated_at timestamp(6) NULL,
     CONSTRAINT todo_pkey PRIMARY KEY (id)
);