CREATE database block_chain;

create table tb_user(
  id bigint(12) not null auto_increment PRIMARY KEY ,
  username VARCHAR(40) not null,
  cell_phone VARCHAR(20),
  email VARCHAR(40),
  password VARCHAR(40),
  sex CHAR(1),
  birth VARCHAR(20),
  create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
  update_at TIMESTAMP DEFAULT  CURRENT_TIMESTAMP
);


