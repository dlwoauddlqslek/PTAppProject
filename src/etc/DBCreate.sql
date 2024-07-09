drop database if exists PtAppProject;
create database PtAppProject;
use PtAppProject;

drop table if exists member;
create table member(				# 회원정보
	memberCode int auto_increment,		# 회원코드(pk)
	ID varchar(20) not null unique,		# 회원ID
	PW varchar(30) not null,			# 회원비밀번호
	memberName varchar(10) not null,	# 회원이름
	height smallint not null,			# 키
	exHabit	tinyint not null,			# 운동습관
	gender char(1) not null,			# 성별
	birthDate varchar(20) not null,		# 생년월일
	contact	varchar(20) not null,		# 연락처
	accCategory tinyint not null,		# 회원분류코드
	primary key (memberCode)
);

drop table if exists food;
create table food(						# 음식
	foodCode int auto_increment,			# 음식코드(pk)
    foodName varchar(20) not null unique,	# 음식이름
    foodKcal smallint not null,				# 음식칼로리(대부분 100g당)
    primary key (foodCode)
);

drop table if exists exercise;
create table exercise (					# 운동
	exCode int auto_increment,				# 운동코드(pk)
    exName varchar(20) not null unique,		# 운동이름
	exKcal smallint not null,				# 운동소모칼로리(10분당)
    exIntensity tinyint not null,			# 운동강도(1~3)
    primary key (exCode)
);

drop table if exists weightrecord;
create table weightrecord(				# 몸무게기록표
	weightCode int auto_increment,			# 몸무게기록코드(pk)
	weight smallint not null,				# 몸무게측정결과
	measureTime datetime default now(),		# 몸무게측정시간
	memberCode int,							# 회원코드(fk)
    primary key (weightCode),
    foreign key (memberCode) references member(memberCode) on delete cascade
);

drop table if exists atefoodrecord;
create table atefoodrecord(				# 먹은음식기록표
	ateFoodCode int auto_increment,			# 먹은음식기록코드(pk)
    foodCode int,							# 음식코드(fk)
    ateTime datetime default now(),			# 먹은음식기록시간
    memberCode int,							# 회원코드(fk)
    primary key (ateFoodCode),
    foreign key (foodCode) references food(foodCode) on delete set null,
    foreign key (memberCode) references member(memberCode) on delete cascade
);

drop table if exists workoutrecord;
create table workoutrecord(				# 운동기록표
	workOutCode int auto_increment,			# 운동기록코드(pk)
    exCode int,								# 운동코드(fk)
    workOutTime datetime default now(),		# 운동기록시간
    memberCode int,							# 회원코드(fk)
    primary key (workOutCode),
    foreign key (exCode) references exercise(exCode) on delete set null,
    foreign key (memberCode) references member(memberCode) on delete cascade
);

drop table if exists message;
create table message (					# 쪽지
	messageCode int auto_increment,			# 쪽지코드(pk)
    sentMCode int,							# 보낸회원코드(fk)
    receivedMCode int,						# 받은회원코드(fk)
    msgTitle varchar(30),					# 쪽지제목
    msgContent text,						# 쪽지내용
	msgView	mediumint,						# 쪽지조회수
	msgDate datetime default now(),			# 쪽지전송시간
    replyContent text,						# 답장내용
    replyDate datetime,		                # 답장시간
    primary key (messageCode),
    foreign key (sentMCode) references member(memberCode) on delete set null,
    foreign key (receivedMCode) references member(memberCode) on delete set null
);

# member 테이블 샘플
INSERT INTO member (memberCode, ID, PW, memberName, height, exHabit, gender, birthDate, contact, accCategory) VALUES ('1', 'admin', 'admin', 'admin', 0, 0, 0, '', '', '1');
INSERT INTO member (memberCode, ID, PW, memberName, height, exHabit, gender, birthDate, contact, accCategory) VALUES ('2', 'member1', '1234', '아무개', '170', '1', 'M', '2000-01-01', '010-111-2222', '2');
INSERT INTO member (memberCode, ID, PW, memberName, height, exHabit, gender, birthDate, contact, accCategory) VALUES ('3', 'pt1', '4321', '김아무개', '173', '2', 'M', '2000-01-01', '010-111-2222', '3');
INSERT INTO member (memberCode, ID, PW, memberName, height, exHabit, gender, birthDate, contact, accCategory) VALUES ('4', 'member2', '1q1q', '이아무개', '167', '3', 'F', '2000-01-01', '010-111-2223', '2');
INSERT INTO member (memberCode, ID, PW, memberName, height, exHabit, gender, birthDate, contact, accCategory) VALUES ('5', 'pt2', '1111', '윤아무개', '170', '2', 'F', '2000-03-02', '010-111-2224', '3');

# food 테이블 샘플
INSERT INTO food (foodCode, foodName, foodKcal) VALUES ('1', '밥', '100');
INSERT INTO food (foodCode, foodName, foodKcal) VALUES ('2', '김밥', '150');
INSERT INTO food (foodCode, foodName, foodKcal) VALUES ('3', '불고기', '200');
INSERT INTO food (foodCode, foodName, foodKcal) VALUES ('4', '된장찌개', '200');
INSERT INTO food (foodCode, foodName, foodKcal) VALUES ('5', '떡볶이', '250');

# exercise 테이블 샘플
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES ('1', '걷기', '30', '1');
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES ('2', '조깅', '60', '2');
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES ('3', '수영', '60', '1');
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES ('4', '자전거타기', '40', '2');
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES ('5', '계단오르내리기', '80', '3');
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (6,'턱걸이',30,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (7,'요가',25,1);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (8,'등산',50,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (9,'줄넘기',70,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (10,'러시안 트위스트',50,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (11,'스쿼트',35,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (12,'필라테스',30,1);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (13,'피트니스 덤벨 운동',50,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (14,'댄스 운동',65,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (15,'버피 운동',50,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (16,'골프',30,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (17,'테니스',50,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (18,'레슬링',60,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (19,'카누',30,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (20,'스키',50,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (21,'스노보드',55,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (22,'아쿠아 에어로빅',50,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (23,'태권도',70,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (24,'바디웨이트 트레이닝',50,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (25,'팔레트톤 사이클링',45,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (26,'바디스컬프팅',40,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (27,'팔레트 트레이닝',40,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (28,'스노우슈',70,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (29,'체조',45,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (30,'스트리트 워크아웃',40,3);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (31,'승마',40,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (32,'사이클링',60,2);
INSERT INTO exercise (exCode, exName, exKcal, exIntensity) VALUES (33,'레이싱',60,3);

# weightrecord 테이블 샘플
INSERT INTO weightrecord (weightCode, weight, measureTime, memberCode) VALUES ('1', '100', '2024-07-02 08:40:15', '1');
INSERT INTO weightrecord (weightCode, weight, measureTime, memberCode) VALUES ('2', '101', '2024-07-02 16:40:50', '1');
INSERT INTO weightrecord (weightCode, weight, measureTime, memberCode) VALUES ('3', '80', '2024-07-02 08:45:42', '2');
INSERT INTO weightrecord (weightCode, weight, measureTime, memberCode) VALUES ('4', '81', '2024-07-02 17:50:11', '2');
INSERT INTO weightrecord (weightCode, weight, measureTime, memberCode) VALUES ('5', '70', '2024-07-02 08:40:12', '3');

# atefoodrecord 테이블 샘플
INSERT INTO atefoodrecord (ateFoodCode, foodCode, ateTime, memberCode) VALUES ('1', '1', '2024-07-02 08:40:15', '1');
INSERT INTO atefoodrecord (ateFoodCode, foodCode, ateTime, memberCode) VALUES ('2', '2', '2024-07-02 16:40:50', '1');
INSERT INTO atefoodrecord (ateFoodCode, foodCode, ateTime, memberCode) VALUES ('3', '1', '2024-07-02 08:45:42', '2');
INSERT INTO atefoodrecord (ateFoodCode, foodCode, ateTime, memberCode) VALUES ('4', '1', '2024-07-02 17:50:11', '2');
INSERT INTO atefoodrecord (ateFoodCode, foodCode, ateTime, memberCode) VALUES ('5', '1', '2024-07-02 08:40:12', '3');

# workoutrecord 테이블 샘플
INSERT INTO workoutrecord (workOutCode, exCode, workOutTime, memberCode) VALUES ('1', '1', '2024-07-02 08:40:15', '1');
INSERT INTO workoutrecord (workOutCode, exCode, workOutTime, memberCode) VALUES ('2', '3', '2024-07-02 16:40:50', '1');
INSERT INTO workoutrecord (workOutCode, exCode, workOutTime, memberCode) VALUES ('3', '2', '2024-07-02 08:45:42', '2');
INSERT INTO workoutrecord (workOutCode, exCode, workOutTime, memberCode) VALUES ('4', '4', '2024-07-02 17:50:11', '3');
INSERT INTO workoutrecord (workOutCode, exCode, workOutTime, memberCode) VALUES ('5', '5', '2024-07-02 08:40:12', '3');

# message 테이블 샘플
INSERT INTO message (messageCode, sentMCode, receivedMCode, msgTitle, msgContent, msgView, msgDate, replyContent, replyDate) VALUES ('1', '2', '3', '안녕하세요', '내용1', '1', '2024-07-02 08:40:15', '답장1', '2024-07-02 16:40:50');
INSERT INTO message (messageCode, sentMCode, receivedMCode, msgTitle, msgContent, msgView, msgDate, replyContent, replyDate) VALUES ('2', '2', '3', '안녕하세요', '내용2', '0', '2024-07-02 16:40:50', null, null);
INSERT INTO message (messageCode, sentMCode, receivedMCode, msgTitle, msgContent, msgView, msgDate, replyContent, replyDate) VALUES ('3', '4', '3', '안녕하세요', '내용3', '1', '2024-07-02 08:45:42', '답장3', '2024-07-02 17:20:11');
INSERT INTO message (messageCode, sentMCode, receivedMCode, msgTitle, msgContent, msgView, msgDate, replyContent, replyDate) VALUES ('4', '4', '5', '안녕하세요', '내용4', '1', '2024-07-02 17:50:11', '답장4', '2024-07-02 17:50:21');
INSERT INTO message (messageCode, sentMCode, receivedMCode, msgTitle, msgContent, msgView, msgDate, replyContent, replyDate) VALUES ('5', '4', '5', '안녕하세요', '내용5', '0', '2024-07-02 08:40:12', null, null);

select foodCode from member inner join atefoodrecord on member.memberCode = atefoodrecord.memberCode where member.memberCode = '1';
select * from atefoodrecord where membercode=2;
select * from workoutrecord where membercode=2;