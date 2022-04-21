create schema api;

create table users (
	user_id VARCHAR(36) not null,
	name VARCHAR(100) not null,
	email VARCHAR(100) not null,
	passwords VARCHAR(512) not null,
	profile_image TEXT,
	primary key(user_id)
);

create table feedback (
	feedback_id VARCHAR(36) not null,
	message text not null,
	created_at timestamp not null,
	is_anonymous boolean not null,
	user_id VARCHAR(36) not null,
	feedback_user_id VARCHAR(36) not null,
	primary key(feedback_id),
	constraint fk_users_user_id
		foreign key (user_id)
		references users (user_id),
	constraint fk_users_feedback_user_id
		foreign key (feedback_user_id)
		references users (user_id)
);

create table tags (
	tag_id VARCHAR(36) not null,
	name VARCHAR(45) not null unique,
	primary key(tag_id)
);

create table feedback_tags (
	feedback_id VARCHAR(36) not null,
	tag_id VARCHAR(36) not null,
	constraint fk_ft_feedback_id
	foreign key (feedback_id)
		references feedback (feedback_id),
	constraint fk_ft_tag_id
	foreign key (tag_id)
		references tags (tag_id)
);
	