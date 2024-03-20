CREATE TABLE users (
      id uuid NOT NULL,
      "password" varchar(255) NULL,
      "role" int2 NULL,
      username varchar(255) NULL,
      CONSTRAINT users_pkey PRIMARY KEY (id)
);