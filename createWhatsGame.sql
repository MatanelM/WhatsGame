CREATE SCHEMA whats_game;

USE whats_game;

# DROP Table gamer;
CREATE TABLE chat
(
	ChatID VARCHAR(36) NOT NULL PRIMARY KEY
);

CREATE TABLE gamer
(
	GamerID VARCHAR(36) NOT NULL,
	Nickname VARCHAR(50) NOT NULL,
	password_enc_hash_256 VARCHAR(64) NOT NULL,
	Email VARCHAR(50) NOT NULL,
	Country VARCHAR(50) NOT NULL,
    ChatID VARCHAR(36) NOT NULL,
	foreign key(ChatID) references chat(ChatID) on delete cascade,
	PRIMARY KEY (GamerID)
);


CREATE TABLE forum
(
	ForumID varchar(36) NOT NULL,
    GamerID varchar(36) NOT NULL,
    GenreID int,
    GameID int,
    FOREIGN KEY(GenreID) REFERENCES genre(GenreID) on delete cascade,
    FOREIGN KEY(GameID) REFERENCES game(GameID) on delete cascade,
    FOREIGN KEY(ForumID) REFERENCES chat(ChatID) on delete cascade,
    FOREIGN KEY(GamerID) REFERENCES gamer(GamerID) on delete cascade
);



CREATE TABLE forum_participants
(
	ForumID varchar(36) NOT NULL,
    GamerID varchar(36) NOT NULL,
    PRIMARY KEY(ForumID, GamerID),
    FOREIGN KEY(ForumID) REFERENCES chat(ChatID),
    FOREIGN KEY(GamerID) REFERENCES gamer(GamerID)
);

CREATE TABLE message
(
	MessageID VARCHAR(36) NOT NULL,
	content VARCHAR(1023) NOT NULL,
	ChatFromID VARCHAR(36) NOT NULL,
	ChatToID VARCHAR(36) NOT NULL,
	SentAt DATETIME,
	foreign key(ChatFromID) references chat(ChatID) on delete cascade,
	foreign key(ChatToID) references chat(ChatID) on delete cascade,
	PRIMARY KEY(MessageID)
);

CREATE TABLE message_forum
(
	MessageID VARCHAR(36) NOT NULL,
    ForumID VARCHAR(36) NOT NULL,
    GamerID VARCHAR(36) NOT NULL,
    IsRead TINYINT default 0, 
    primary key(MessageID, ForumID, GamerID),
    foreign key(MessageID) references message(MessageID) on delete cascade,
    foreign key(ForumID) references forum(ForumID) on delete cascade,
	foreign key(GamerID) references gamer(GamerID) on delete cascade
);
# on adding message to forum -
# for each participant in the forum but the sender
# add messageID, ForumID, participantID, 0
# to the message forum

# on fetching notifications - 
# fetch only those who is not related to the forum
# then get those who is notifications of unread messages of person in the chat

# on marking read notification - if it is a forum message - update the is read on the message_forum

CREATE TABLE notification
(
	NotificationID VARCHAR(36) NOT NULL,
    Type VARCHAR(36) NOT NULL,
    isRead TINYINT default 0, 
	PRIMARY KEY(NotificationID)
);

CREATE TABLE gamers_friends
(
	FriendRequestID varchar(36) not null,
    SenderID varchar(36) not null,
    ReceiverID varchar(36) not null,
    isAccepted BOOLEAN not null DEFAULT 0,
    SentAt DATETIME,
    PRIMARY KEY(SenderID, ReceiverID),
    foreign key(SenderID) references gamer(GamerID) on delete cascade,
    foreign key(ReceiverID) references gamer(GamerID) on delete cascade
);

# trigger
delimiter //
create trigger add_notification_friend
AFTER INSERT
on gamers_friends
FOR EACH ROW
BEGIN
	iNSERT INTO notification (NotificationID, Type) VALUES(NEW.FriendRequestID, 'FRIEND');
END;//
delimiter ;

# trigger
delimiter //
create trigger add_notification_message
AFTER INSERT
on message
FOR EACH ROW
BEGIN
	iNSERT INTO notification (NotificationID, Type) VALUES(NEW.MessageID, 'MESSAGE');
END;//
delimiter ;


# trigger
delimiter //
create trigger add_chat_on_new_gamer
BEFORE INSERT
on gamer
FOR EACH ROW
BEGIN
	iNSERT INTO chat (ChatID) VALUES(New.ChatID);
END;//
delimiter ;


# trigger
delimiter //
create trigger add_chat_on_forum
BEFORE INSERT
on forum
FOR EACH ROW
BEGIN
	iNSERT INTO chat (ChatID) VALUES(New.ForumID);
    
END;//
delimiter ;

# trigger
delimiter //
create trigger add_participant_on_forum
AFTER INSERT
on forum
FOR EACH ROW
BEGIN
	iNSERT INTO forum_participants (ForumID, GamerID) VALUES(New.ForumID, New.GamerID);
END;//
delimiter ;

CREATE TABLE genre(
   GenreID   int auto_increment,
   Genre       VARCHAR(23) NOT NULL 
  ,Explanation VARCHAR(651) NOT NULL,
  PRIMARY KEY(GenreID, Genre)
);

CREATE TABLE gamer_genre(
   GenreID   int,
   GamerID    VARCHAR(36) NOT NULL,
   FOREIGN KEY(GamerID) REFERENCES gamer(GamerID) ON DELETE CASCADE,
   FOREIGN KEY(GenreID) REFERENCES genre(GenreID) ON DELETE CASCADE
);

CREATE TABLE game(
	GameID int auto_increment,
    Game VARCHAR(255),
    GenreID int,
    PRIMARY KEY(GameID, Game),
    FOREIGN KEY(GenreID) REFERENCES genre(GenreID) ON DELETE CASCADE
);

CREATE TABLE gamer_game(
	GameID int auto_increment,
    GamerID VARCHAR(36) NOT NULL,
    PRIMARY KEY(GameID, GamerID),
    FOREIGN KEY(GameID) REFERENCES game(GameID) ON DELETE CASCADE,
    FOREIGN KEY(GamerID) REFERENCES gamer(GamerID) ON DELETE CASCADE
);

INSERT INTO genre(Genre,Explanation) VALUES ('2D Fighting','Fighting games usually feature one-on-one combat where opponents battle until a certain outcome, usually the depletion of one fighter''s health. Though early 2D Fighting games offered varying interpretations of this concept, Street Fighter II defined the genre at large, featuring a roster of varied characters with a multitude of special moves, which can be chained into combos. This type of highly technical game became the standard for the genre and continues to have a huge worldwide competitive player base today. Despite losing popularity with the death of arcades in the West, Fighting games have made a comeback thanks to online console play.');
INSERT INTO genre(Genre,Explanation) VALUES ('3D Fighting','After Street Fighter II set the template for 2D Fighting games, titles like Tekken and Virtua Fighter became pioneers of the 3D equivalent. 3D Fighting games have the same basic structure as their 2D counterparts, but combat on a 3D plane allows for more dodging, horizontal attack ranges, and a greater importance for grappling and "juggling" enemies in midair. 3D Fighting games also tend to be more slow-paced than 2D Fighting games.');
INSERT INTO genre(Genre,Explanation) VALUES ('4X Strategy','A sub-genre of Strategy games focused on the four X''s: eXplore, eXpand, eXploit, and eXterminate. These games are large-scale and take place over long periods of time, giving the player control over the growth and development of entire civilizations. Usually they feature some type of combat, which may be turn-based or real-time, and a historical or sci-fi setting. These games were very popular on PC in the mid-to-late nineties.');
INSERT INTO genre(Genre,Explanation) VALUES ('Action','One of the broadest game genres. Action games usually revolve around some type of combat where the player controls a character or avatar, and require fast reflexes and hand-eye coordination to overcome obstacles and defeat enemies. Many of the earliest video games are considered Action games, and most modern games contain Action elements as well. However, most modern games fall into one of the many Action sub-genres and should be labeled as such.');
INSERT INTO genre(Genre,Explanation) VALUES ('Action-Adventure','A hugely popular game genre that combines the reflex-based gameplay of Action games with the long-term obstacles and narrative of Adventure games. The player goes on a journey while obtaining new items or abilities, overcoming small and great obstacles, and defeating ever-stronger enemies. A lot of games that don''t neatly fit into any particular subgenre are labeled Action-Adventures if they have elements of both.');
INSERT INTO genre(Genre,Explanation) VALUES ('Action-RPG','A subgenre of RPGs, Action-RPGs incorporate a combat and gameplay system taken from Action games. This makes gameplay more brisk and exciting than the usually cerebral or slow-paced combat of RPGs. Action-RPGs tend to follow the structure of Action-Adventures, but still contain the stats, leveling, and equipment systems that identify them as RPGs.');
INSERT INTO genre(Genre,Explanation) VALUES ('Adventure','Adventure is one of the oldest game genres, and refers to a type of game that doesn''t include action gameplay or reflex-based challenges. Rather, players explore their environment, interact with characters, and solve puzzles to progress. Traditional Adventure games usually featured a text-based or "point-and-click" interface. In Japan, Visual Novels are a popular sub-genre. Pure Adventure games lost popularity in the late nineties, but have recently experienced a comeback, mainly thanks to indie developers.');
INSERT INTO genre(Genre,Explanation) VALUES ('Air Combat','A sub-genre of Combat games that simulates dogfights betweeen planes, or similar showdowns with airships. These games may vary in their use of first or third-person perspective and their degree of realism. There are some variations, such as space combat games where players pilot spaceships, but the general gameplay is similar.');
INSERT INTO genre(Genre,Explanation) VALUES ('Arena Brawler','A sub-genre of Fighting games that usually features larger stages with elements that can be interacted with, Platform game elements, item pick-ups, combat for more than two players at once, and simpler move inputs compared to traditional Fighting games. Arena Brawlers may also be single-player games with progression similar to Beat-''em-ups. This sub-genre was popularized by the Super Smash Bros. series, but pioneered by Power Stone in the previous generation. Many licensed games based on anime series and cartoons are Arena Brawlers. They are often seen as more "casual" Fighting games, but may boast deep gameplay.');
INSERT INTO genre(Genre,Explanation) VALUES ('Arena Combat','A sub-genre of Combat games where players choose a character from a roster like in Fighting games. However, combat takes place in large arenas where players have high mobility and access to a wide arsenal of long-range moves. Many Mech Combat titles are Arena Combat games as well. In the mid-2010s the genre has become quite popular in Japanese arcades for its fast-paced gameplay and potential to be adapted from many franchises.');
INSERT INTO genre(Genre,Explanation) VALUES ('Arena Shooter','Arena Shooters are a spin-off of Shooting games, and First-Person Shooters in particular. Though FPS games like Halo and 007: GoldenEye boasted prominent multi-player features, Arena Shooters are dedicated multi-player experiences where players usually choose their character from a pre-determined roster (this version is also known as a "Hero Shooter"). Arena Shooters are often squad-based and feature characters with different roles and specializations, giving matches a certain degree of tactics.');
INSERT INTO genre(Genre,Explanation) VALUES ('Artillery','Artillery games are a very old sub-genre of Strategy, where the player controls tanks or similar vehicles and fires projectiles at opponents. The core of the gameplay is in calculating the trajectories of projectiles in order to successfully hit enemies. The oldest Artillery games were text-based, but they reached a peak of popularity with modern online iterations, like Gunbound, becoming massive hits. Today, Artillery games persist mostly through a handful of established franchises.');
INSERT INTO genre(Genre,Explanation) VALUES ('Atmospheric','This is a term used to refer to games that are primarily about atmospheric exploration instead of fulfilling immediate objectives. The game may not even have any specific goals other than soaking up the atmosphere, but in any case the player is encouraged to explore to their heart''s content. Games that have atmospheric settings but clear, constant objectives should probably not be classified as Atmospheric games, as a rule. Free-roaming diving games usually fit into this category as well.');
INSERT INTO genre(Genre,Explanation) VALUES ('Beat-''em-Up','A sub-genre of Action games and a cousin of Fighting games. Players choose their character, with a particular arsenal of moves and abilities, and fight their way through side-scrolling stages full of mooks, while facing the occasional boss, often in an urban setting. Beat-''em-ups usually feature item pick-ups and multi-player action. This last feature made them widely popular on arcades throughout the nineties, after which they lost relevance and now appear mainly as download titles or on the indie market. They continue to hold a devoted fanbase.');
INSERT INTO genre(Genre,Explanation) VALUES ('Board Game','A game that simulates the experience of playing a board game, whether this is a real and established game such as Monopoly or mahjong, or a fantasy board game such as Mario Party or Fortune Street. These may be faithful recreations of the games they are based on, or take several liberties with the concept. Usually multi-player, obviously.');
INSERT INTO genre(Genre,Explanation) VALUES ('Bullet Hell','Bullet Hell (known as "Danmaku" in Japan) games are a specific type of Shoot-''em-up. Like the name implies, Bullet Hell games feature enemies that literally fill the screen with complex, interweaving patterns of projectiles. Thus the player''s main challenge consists not in defeating their enemies but in successfully navigating the onslaught of projectiles without dying. Bullet Hell games were established by titles like DoDonpachi and popularized by the Touhou series of indie games, and are noted for their extreme difficulty.');
INSERT INTO genre(Genre,Explanation) VALUES ('Card Game','A game that simulates playing a card game. The most popular and ubiquitous example is Solitaire, available on just about every computer ever. Over the past decade, Card Games have gained new relevance thanks to the rise of online poker.');
INSERT INTO genre(Genre,Explanation) VALUES ('Casual','Although it has been often used derisively (contrasted with "hardcore" games), Casual is an overarching category that describes simple, easy, and accessible games generally on browsers or mobiles, meant for people who generally don''t spend much time playing video games. These games may be watered-down versions of established genres or just simple Action games, much like the earliest arcade titles. Casual games are designed to be played on-the-go, and in shot bursts.');
INSERT INTO genre(Genre,Explanation) VALUES ('Character Action','A recently-defined sub-genre of Action games. In Character Action games, players take control of a character with a huge arsenal of weapons and techniques, and a complex moveset which includes combos and move variations. Though it may have the structure of an Action-Adventure, the focus of Character Action games is the complex combat system, and finishing enemies in the most stylish way possible. Players are usually graded and rewarded for their performance in this regard. Also called Stylish Action or Extreme Action.');
INSERT INTO genre(Genre,Explanation) VALUES ('CMS','CMS (Construction and Management Sims) are a type of Sim game where players must build, maintain and expand a community or project while managing limited resources. Success is often measured by the quality of life in the community or similar metrics. There are many sub-types of CMS games, such as business sims, government sims, and theme park sims.');
INSERT INTO genre(Genre,Explanation) VALUES ('Collect-a-thon Platform','Collect-a-thon Platform games are a specific type of 3D Platform game that became very popular in the mid-to-late nineties thanks to games like Super Mario 64 or Banjo-Kazooie. Their defining characteristic is the existence of dozens if not hundreds of collective trinkets spread out all over playground-like stages, and players must find and collect them all. This may be the game''s primary goal or simply an optional objective, but in any case the game is crammed with collectibles.');
INSERT INTO genre(Genre,Explanation) VALUES ('Combat','Combat is a cousin of the Action genre that focuses specifically on simulated combat in a specific situation. There are many established Combat sub-genres such as Vehicular Combat, Air Combat, and Mech Combat. These games usually aim to give a degree of realism to the experience without turning into Sim games. They often include a degree of customization as well. Combat games in various forms have always had their fanbase on consoles and PC.');
INSERT INTO genre(Genre,Explanation) VALUES ('Dating Sim','Dating Sims are a popular sub-category of Visual Novels, which have the same presentation and gameplay as their parent genre. However, the player''s main objective is to pursue a character as romantic interest, usually given the choice from several available ones. The romantic interest that the player chooses will usually determine the story branch that they follow and the ending they get. These games may or may not have erotic elements (known as "eroge"). Though Dating Sims usually feature a male pursuing females, there are "otome" games that reverse the roles, as well as "yaoi" and "yuri" games for gay and lesbian relationships, respectively.');
INSERT INTO genre(Genre,Explanation) VALUES ('Dungeon Crawler','Dungeon Crawlers are a sub-genre of RPG primarily focused on dungeon exploration and combat, mainly inspired by the Wizardry games. Narrative and overarching story may take a back seat to exploring dungeons, killing monsters, and collecting loot or money. Unlike Roguelikes or Hack-n-Slash games, Dungeon Crawlers are defined by usually featuring a first-person perspective and a turn-based combat system, though there are active-time examples as well. Dungeon Crawlers also tend to feature puzzles and stronger Adventure game elements than the other genres mentioned.');
INSERT INTO genre(Genre,Explanation) VALUES ('Edutainment','This is a broad category of games that seek to entertain while they educate players or teach them new skills. Edutainment games include Bible story games, language-learning games, history trivia games, problem-solving games, and many others. As long as they''re video games and they''re expressly designed to teach the player something or improve a real-world skill, they fit into this category.');
INSERT INTO genre(Genre,Explanation) VALUES ('Endless Runner','These are games where the player''s avatar is constantly running through a procedurally-generated, endless stage. Players can usually jump or turn to avoid obstacles, among other actions, but cannot stop running. Endless Runners also have driving and other equivalents, and have become especially popular on mobiles.');
INSERT INTO genre(Genre,Explanation) VALUES ('Escape the Room','A sub-genre of Adventure games that has been popular with the indie development community for decades, Escape the Room games place the player inside a locked room. Predictably, the main challenge consists in using whatever tools are available to escape said room. Whether these sequences are framed within a larger story or exist individually depends on the game. Some Adventure games include Escape the Room segments as part of their puzzles.');
INSERT INTO genre(Genre,Explanation) VALUES ('FPS','FPS (First-Person Shooter) games are a sub-genre of Shooting games that became immensely popular. They are basically Shooting games played from the protagonist''s perspective, allowing for a higher degree of immersion and realism. Military FPS games are extremely popular, but these games may also take place in fantasy or sci-fi settings. FPS games may also incorporate an optional third-person perspective, limited melee combat, or narrative elements of Action-Adventures. Online multi-player FPS games are also particularly popular.');
INSERT INTO genre(Genre,Explanation) VALUES ('Grand Strategy','A sub-genre of Strategy directly inspired by tabletop wargames. In Grand Strategy games, the player mobilizes an entire community or country''s resources, and must consider a multitude of variables when taking action. These games are usually very complex and take place in a real-world historical period, with sessions that can last hours and battles that can last days. However, there are also simplified and more streamlined entries in the genre.');
INSERT INTO genre(Genre,Explanation) VALUES ('Hack-n-Slash','Hack-n-Slash is a term often used to describe all kinds of Action games today, but originally it was used to describe Diablo and games like it. These are action-RPGs, usually with a multi-player component, where characters explore dungeons, defeat enemies, obtain loot, purchase new weapons and items, and repeat the process ad nauseaum. However, this usually takes place within the larger structure of a traditional RPG story and progression, which differentiates these games from the more barebones Roguelikes. Online Hack-n-Slash games are also very popular.');
INSERT INTO genre(Genre,Explanation) VALUES ('Incremental','Incremental games, also called idle games, clicker games or clicking games, are video games whose gameplay consists of the player performing simple actions (such as clicking on the screen) repeatedly to gain currency. This can be used to obtain items or abilities that increase the rate at which currency accrues.');
INSERT INTO genre(Genre,Explanation) VALUES ('Interactive Fiction','Interactive Fiction games refer to early Adventure games which used text-based input for the player to choose their next action. They also refer to more modern games that blur the line between "games" and "interactive storytelling." In any case they are Adventure games with a text-based presentation and an overwhelming focus on narrative over everything else, similar to Japanese Visual Novels but often less character-centric.');
INSERT INTO genre(Genre,Explanation) VALUES ('Interactive Movie','Interactive Movies are a sub-genre of Adventure games that feature full-motion animations or live-action sequences, which play out like a movie. At certain points the player will have to input an action in order to progress or fail. Usually the player''s only involvement in these games is guessing which action the developers want them to take. While initially maligned thanks to a drove of bad games in the nineties, Interactive Movies have made a comeback thanks to games with more involving and less contrived gameplay structure.');
INSERT INTO genre(Genre,Explanation) VALUES ('JRPG','JRPGs (Japanese RPGs) are a type of RPG primarily developed by Japanese studios and usually contrasted with Western RPGs for their different characteristics. The trademarks of JRPGs include a pre-determined party of characters with little customization, relatively linear progression, a fixed story with little player agency, a turn-based battle system, and an anime art style. JRPGs were massively popular in the mid-to-late nineties but lost favor in the next decade. Recently JRPGs have evolved beyond their constraints, while more traditional offerings are still available mostly on handheld systems.');
INSERT INTO genre(Genre,Explanation) VALUES ('Kart Racing','Kart Racing games are a sub-genre of Racing games popularized by Mario Kart. Aside from normally featuring karts instead of other vehicles, these Racing games are less serious, often featuring fantasy characters and settings, unrealistic physics, and item pickups which can be used against opponents. The general structure of Kart Racing games makes them more amenable to casual players than "serious" and demanding Racing titles, though that doesn''t mean they aren''t competitive or complex in gameplay.');
INSERT INTO genre(Genre,Explanation) VALUES ('Kusoge','Kusoge literally means "shit game" in Japanese. However, it is not used to refer to all bad games in general. Specifically, a Kusoge is a game that is unquestionably, objectively bad, but still enjoyable or interesting for a number of reasons. Maybe it has personality, an interesting gameplay feature, or is just stupidly ridiculous, but in any case it might be worth a play for the more adventurous gamer.');
INSERT INTO genre(Genre,Explanation) VALUES ('Life Sim','Life Sims are a type of Sim game that put the player in the shoes of a character in the real world or a fictional one, who is simply living their life. The focus is on daily micro-management of resources, income earning, and miscellaneous activities. The game may have an ultimate objective or none at all. Life Sims where the player controls the actions and/or environment of multiple people, such as The Sims, are also referred to as "God Games," and generally don''t have a fixed primary objective.');
INSERT INTO genre(Genre,Explanation) VALUES ('Light Gun','Light Gun games are a sub-genre of Shooting games primarily designed for arcades. Players normally use a gun-like accessory or peripheral to directly shoot at the screen with a sensor. Due to their format, they are usually played from a first-person perspective. Light Gun games lost popularity with the death of arcades in the West, but have experienced a revival thanks to motion controls appearing on consoles. Light Gun games may also be Rail Shooters if movement is on-rails, but this isn''t necessarily the case.');
INSERT INTO genre(Genre,Explanation) VALUES ('Matching Puzzle','Matching Puzzle games are a sub-genre of Puzzle games established and defined by the ever-popular Tetris. Players have to place blocks or objects into lines as they fall, trying to put objects of the same shape or color together in order to "clear" the line and continue playing. Some Matching Puzzle games have a competitive multi-player element, where players try to ruin their opponent''s game with specific power-ups or special abilities.');
INSERT INTO genre(Genre,Explanation) VALUES ('Maze','Maze games were popularized by Pac-Man and enjoyed a period of ubiquity before more or less disappearing. In Maze games, the player character has to navigate and potentially escape maze-shaped levels, usually while negotiating traps or enemies in pursuit. Some modern games with an isometric or top-down perspective present Action or Shooting gameplay in maze-like environments.');
INSERT INTO genre(Genre,Explanation) VALUES ('Mech Combat','A specific type of Combat game where the player controls a mecha, a giant robot or power suit that is popular in Japanese anime and pop culture. As such, this is an almost exclusively Japanese genre. Mech Combat games usually play similarly to Arena Combat or Vehicular Combat games, but usually include projectiles, melee combat, fast travel, and flight options. Mech Combat games may take on the structure of Action-Adventures or consist only of one-on-one showdowns.');
INSERT INTO genre(Genre,Explanation) VALUES ('Metroidvania','The term "Metroidvania," a portmanteau of "Metroid" and "Castlevania," was originally coined to describe the gameplay of Castlevania: Symphony of the Night. These are usually side-scrolling Action-Adventures with RPG elements. They are primarily defined by their large, fully interconnected game world, which allows for non-linear progression, as the player obtains items and abilities that allow them to explore previously inaccessible areas. While both Metroid and Castlevania have since moved away from this style of game, it is very popular in the indie scene.');
INSERT INTO genre(Genre,Explanation) VALUES ('Military Sim','This type of Sim game tries to realistically simulate the experience of military combat, whether modern or belonging to a certain historical period. While modern Military Sims may superficially look like FPS games, they lack the unrealistic aspects and video game-y mission objectives of those games. Combat is more tactical than twitch-based, and there are many more variables for players to consider. Lately, online Military Sims have become remarkably popular.');
INSERT INTO genre(Genre,Explanation) VALUES ('Mini-games','This sub-genre of Action games refers to titles which may have an overarching gameplay system and/or narrative, but their main content is split into scores of mini-games which can be completed in short periods of time. Mini-games are included in all sorts of other game genres, but this genre refers specifically to games with a gameplay mechanic focused on lots of mini-games, played either procedurally or in random bursts. Multi-player games with lots of mini-games are also called "Party Games."');
INSERT INTO genre(Genre,Explanation) VALUES ('MMO','MMO (Massive Multi-player Online) games allow players to connect with dozens, hundreds, or thousands of others in a persistent game world. MMO games are not a genre unto themselves; there are MMORPGs, MMORTS games, MMOFPS games, and so on. Their defining characteristic is that they are primarily games to be played online with others en masse. Games which have an online component but are primarily single-player experiences should not be called MMO games.');
INSERT INTO genre(Genre,Explanation) VALUES ('MOBA','MOBA (Multi-player Online Battle Arena) games were popularized by DOTA 2, which set the template for the genre. Players choose characters from a roster with pre-determined abilities and face off in teams on large arenas, co-operating to win against the opposing team. MOBA games incorporate elements of RPGs (leveling up, purchasing items), and Strategy games (defending towers, managing henchmen). The MOBA competitive scene is especially lucrative and popular.');
INSERT INTO genre(Genre,Explanation) VALUES ('Monster Collecting','Monster Collecting games are a type of RPG that became very popular thanks to the PokEmon series. These games normally progress as typical JRPGs but instead of fighting enemies directly, the player can catch, raise, strengthen, and put together teams of monsters or wild beasts. This sub-genre fuses elements of RPGs with Raising Sims, but normally with a focus on combat. Some RPGs include a monster-collecting side-game or feature, without being full-fledged Monster Collecting games.');
INSERT INTO genre(Genre,Explanation) VALUES ('Musou','Musou is a type of Action game popularized by the Dynasty Warriors series. Players choose from a roster of pre-determined characters with a number of special attacks, and progress through large areas full of low-level enemies or mooks, occasionally facing a boss. Musou games are distinguished because progress is made by killing hundreds if not thousands of enemies in crowd-clearing super attacks, pushing your way through endless waves of foes. Recently, many Musou games based on established properties have been produced, due to their simple but addictive gameplay.');
INSERT INTO genre(Genre,Explanation) VALUES ('Open World','Open World (or "sandbox") games are defined by their non-linear progression. They usually make all or nearly all of the game world available to the player from the start, allowing them to go and explore in whatever direction they like. While Open World games usually have an overarching story and objective, these may take a back seat compared to whatever the player wants to do. Open World-style games became hugely popular in the late 2000s, especially for RPGs and Action-Adventures.');
INSERT INTO genre(Genre,Explanation) VALUES ('Paddle','Paddle games are a style of early game that was popular in arcades, mostly inspired by the ever-popular Pong and Breakout. Players manipulate some type of paddle or reflector in order to bounce back a ball either at an objective or at their opponent. There are countless Breakout and Pong clones out there, but in any case they all have more or less the same gameplay.');
INSERT INTO genre(Genre,Explanation) VALUES ('Party','Party games are multi-player games meant specifically for a social setting. They may be video game versions of real-world game shows or original games. Party games usually feature accessible gameplay and games based on rounds, where players take turns competing to emerge victorious.');
INSERT INTO genre(Genre,Explanation) VALUES ('Physics Puzzle','A sub-genre of Puzzle games. Players have to figure out the physics engine of a game in order to take advantage of its properties and complete challenges by manipulating objects. 2D Physics Puzzle games are very popular on mobiles, while their 3D equivalent reached a new level of popularity with the development of open-source physics engines for games like Half-Life 2 and Amnesia. Physics Puzzles are included in many first-person Action-Adventure and FPS games.');
INSERT INTO genre(Genre,Explanation) VALUES ('Platform','Platform games are a sub-genre of Action games where the focus is on overcoming physical obstacles through careful maneuvering and jumping. This usually involves hopping on platforms, hence the name. They usually include some kind of combat, and an Action-Adventure game structure. 2D and 3D Platform games are sometimes separated due to their considerably different mechanics, but they operate under the same concepts. A notable sub-genre is the Collect-a-thon Platform game, which gained popularity in the nineties.');
INSERT INTO genre(Genre,Explanation) VALUES ('Pro Wrestling','Pro Wrestling games are a sub-genre of Sports games that have developed their own conventions and gameplay systems. Due to their nature they are similar to Fighting or Arena Combat games, but focused more on grappling and strategically-timed holds in order to succeed. Pro Wrestling games may be licensed and feature real-world wrestlers, or be entirely fictional. They usually boast very large rosters and character creators.');
INSERT INTO genre(Genre,Explanation) VALUES ('Programming','This is a niche type of game that enjoys certain popularity on PC. In single-player Programming games, players must master and use a programming language mainly to solve puzzles; in multi-player Programming games, two players'' programs are pitted against each other. The latter category has a wide competitive scene for all types of games, including tournaments for chess-playing programs.');
INSERT INTO genre(Genre,Explanation) VALUES ('Puzzle','A basic game genre, in Puzzle games players must, obviously, solve puzzles to progress. They may also be video game versions of real-world established types of puzzles, like Sudoku or trivia games. Pure Puzzle games are now rare, but they have been incorporated into all sorts of game genres, including Action-Adventures, RPGs, and even FPS games. The Matching Puzzle sub-genre remains popular, as do Adventure games focused on puzzle-solving, and Platform games with Puzzle elements.');
INSERT INTO genre(Genre,Explanation) VALUES ('Racing','One of the earliest game genres, in Racing games the player must simply race and beat opponents to the finish line. Racing games usually but not always involve vehicles, and have different degrees of realism. The most realistic ones might qualify as Vehicular Sims, while the more fantastical ones might fall into the Vehicular Combat or Kart Racing sub-genres. At any rate, Racing is a perennially popular genre on arcades, PCs and consoles, and many games of other genres have incorporated Racing or vehicular features.');
INSERT INTO genre(Genre,Explanation) VALUES ('Rail Shooter','A 3D sub-genre of the Shooting genre. Rail Shooters are close relatives of Light Gun games. Players progress through a mostly pre-determined path while manipulating a cursor to shoot enemies on the screen. Depending on the game, progression through levels may be completely on-rails or allow for a degree of movement. The latter category is sometimes called a "Tube Shooter" and comprises games like StarFox and Sin & Punishment.');
INSERT INTO genre(Genre,Explanation) VALUES ('Raising Sim','Raising Sims are a type of Sim game where the player is in charge of raising and taking care of something or someone, usually a pet. Raising Sims find their origins in portable Tamagotchi games of the late eighties and nineties, but have since evolved into more complex titles. Many games, especially Monster Collecting games, have incorporated elements of Raising Sims in order to have the player establish an emotional connection with the game''s characters.');
INSERT INTO genre(Genre,Explanation) VALUES ('Rhythm','Rhythm games are a sub-genre of Action games where players need to time actions and button presses to a beat or song in order to succeed. Rhythm games that use peripherals such as Dance Dance Revolution and Guitar Hero became wildly popular for a time. Even though they are past their peak, Rhythm games continue to enjoy success mostly on handheld systems and PC. Many modern Rhythm games have a competitive or multi-player element as well.');
INSERT INTO genre(Genre,Explanation) VALUES ('Roguelike','Roguelikes are a sub-genre of RPGs directly inspired by the PC game Rogue, which featured randomly-generated levels as players progressed through dungeons. The focus of Roguelikes is on slaying monsters, collecting loot, and acquiring better equpment to proceed farther into the dungeon. There are usually harsh penalties for dying, such as losing all your equipment and/or experience. Unlike other RPGs, there is usually little or no focus on story. Modern Roguelikes usually feature real-time combat.');
INSERT INTO genre(Genre,Explanation) VALUES ('RPG','RPGs (Role-Playing Games) are a wide and popular genre, usually based on the concepts of tabletop role-playing games like Dungeons & Dragons. Players take control of a character or characters in a fictional setting and progress through the game by acquiring equipment, increasing their character''s stats, and/or leveling up through experience. Since their appearance, elements of RPGs have been applied to all kinds of games, but "true" RPGs usually retain focus on story and character-building. Cultural differences have led to the labels "Western RPG" and "Japanese RPG" to describe different design approaches.');
INSERT INTO genre(Genre,Explanation) VALUES ('RTS','RTS (Real-Time Strategy) is a sub-genre of Strategy games, where all of the player''s decision-making and actions take place in a constantly-changing environment. RTS games are characterized by actions like producing units, fortifying bases, gathering materials, and researching technologies. RTS games have acquired a vibrant competitive scene, with games such as StarCraft II being played competitively for huge sums in South Korea. Modern RTS games are generally on PC and incorporate a multi-player element.');
INSERT INTO genre(Genre,Explanation) VALUES ('Run-n-Gun','Run-n-Gun is a sub-genre of Shooting games. Similar to Shoot-''em-ups, the player controls a character or avatar while shooting enemies through scrolling levels. However, Run-n-Gun games incorporate elements of Platform games and Action-Adventures in their gameplay. There are both horizontally and vertically-scrolling Run-n-Guns. The genre has fallen out of popularity, having reached its peak in the mid-to late nineties.');
INSERT INTO genre(Genre,Explanation) VALUES ('Shoot ''em Up','Shoot-''em-ups are a subgenre of Shooting games where the player controls an avatar (usually an airship) and shoots enemies while progressing through scrolling levels. Shoot-''em-ups are usually light on narrative and focus on arcade-style gameplay, including power-ups, score systems, and end-stage bosses. While Shoot-''em-ups reached peak popularity along with arcades in the nineties, they have since retained a strong cult following, with new entries on PC being quite common. Shoot-''em-ups with cutesy art direction, like Cotton or TwinBee, are affectionately known as "Cute-''em-ups."');
INSERT INTO genre(Genre,Explanation) VALUES ('Shooting','Shooting is one of the most broad genre descriptors. Basically, players control a character or avatar and shoot enemies or objectives. Shooting games may be in 2D (Run-n-Gun, Shoot-''em-Up) or 3D (FPS, TPS), and feature elements from several other genres (Platform, Action-Adventure, Puzzle). Generally, Shooting games should be further specified through sub-genres when they are labeled as such.');
INSERT INTO genre(Genre,Explanation) VALUES ('Sim','Sim is a very broad genre, and an abbreviation for "simulation." Put simply, Sim games aim to give players a realistic (or simply immersive) simulation of a certain situation or experience. There are many established sub-categories of Sim games, such as Vehicular Sims, Life Sims, Construction and Management Sims, and Raising Sims, among many others. Sim games should be further defined through these sub-categories.');
INSERT INTO genre(Genre,Explanation) VALUES ('Sports','Sports is a very broad category that refers to video games based on all manner of sports. The sport in question may be real or fictional, and the game may approach it with a realistic or unrealistic tone. Generally speaking, Sports games should be further defined by the specific sport that they are about, but there are too many to list them as sub-genres in this section.');
INSERT INTO genre(Genre,Explanation) VALUES ('SRPG','SRPGs (Strategy RPGs) are a sub-genre of RPGs that were primarily defined by games like Fire Emblem and Shining Force. In these games, players control several characters on large battlefields and attack enemies in turns. The concepts of RPGs such as experience, leveling, equipment, and stats normally apply. The combat system of RPGs incorporates an element of Strategy games, but they should not be confused with TBS games. SRPGs feature the characters and narrative commonly associated with RPGs.');
INSERT INTO genre(Genre,Explanation) VALUES ('Stealth','Stealth games are a sub-genre of Action-Adventures where the player is encouraged to hide, sneak around, and attack enemies by surprise instead of engaging them directly. Aside from their approach to combat, Stealth games are usually very similar to Action-Adventures. Stealth elements have also been incorporated into more action-oriented games, but a game is only considered to fit into this genre if approaching enemies in a non-stealthy way is usually discouraged. Stealth games rose to prominence in the early 2000s.');
INSERT INTO genre(Genre,Explanation) VALUES ('Strategy','Strategy is a broad term to refer to games that incorporate elements of tactics and/or war games to their gameplay. Players must usually manage large numbers of units, various different resources, and attempt to expand their group and/or defeat an enemy. Strategy games may feature turn-based or real-time combat, and boast varying levels of complexity, but normally require a more cerebral approach to problems in order to succeed.');
INSERT INTO genre(Genre,Explanation) VALUES ('Survival','A sub-genre of Action-Adventure games where the player character primarily needs to survive. Usually the player character has several needs (hunger, rest, sanity) which need to be periodically sated by finding resources in a hostile world. There is usually a combat feature, as well as elements of RPGs such as item-crafting or earning experience. The game may have a fixed goal, or may simply end when the player character finally dies. Many Action-Adventure games have incorporated Survival elements without becoming Survival games.');
INSERT INTO genre(Genre,Explanation) VALUES ('Survival Horror','Survival Horror is a type of Action-Adventure game that was primarily defined by Resident Evil. Players control a character who is relatively helpless against the enemies they will face, which encourages hiding or running away more often than fighting. They are also characterized by scarce items/ammunition, persistent enemies, and control schemes that limit the player''s mobility. As the name suggests, these games usually have horror movie settings, which adds to the tension. Although Survival Horror games are not as popular as they used to be, elements of them can be found in many modern Action-Adventures.');
INSERT INTO genre(Genre,Explanation) VALUES ('TBS','TBS (Turn-Based Strategy) games are a sub-genre of Strategy games, and the turn-based counterpart to RTS games. Players must manage several units, resources and objectives in order to complete missions, but combat and other actions are carried out in turn-based order. TBS games have fallen out of favor in recent years, largely replaced by the mechanically similar SRPG, but some still persist.');
INSERT INTO genre(Genre,Explanation) VALUES ('TCG','TCGs (Trading Card Games) are the video game versions of (usually) established, real-world trading card games. The game may work with a peripheral that scans the player''s real-world card collection, or may hand out virtual decks for the player to use (or both). TCGs usually stick to the rules of their real-world counterpart, if there is one.');
INSERT INTO genre(Genre,Explanation) VALUES ('Time Management','A quick-thinking sub-genre of Strategy where players must manage limited time and resources in order to complete a steady stream of tasks. Time Management games may have Sim elements depending on the activity involved. They have become popular games for handheld systems and mobiles thanks to their accessibility.');
INSERT INTO genre(Genre,Explanation) VALUES ('Tower Defense','Tower Defense games are a sub-genre of Strategy games where the focus is on defending a main base or tower from incoming waves of low-level enemies or "creeps." The player must implement, upgrade, and use all manner of weapons to impede their advancement, while managing resources and placing new towers in real time. Tower Defense games have recently become very popular on browsers and platforms thanks to their accessibility and quick play style.');
INSERT INTO genre(Genre,Explanation) VALUES ('TPS','TPS (Third-Person Shooter) games are a sub-genre of Shooting games. The player controls a character and views the action from the third-person, usually over-the-shoulder perspective. Compared to FPS games, TPS games are more likely to incorporate elements of RPGs or Platform games. Modern TPS games also tend to feature cover mechanics (giving rise to the term "Cover Shooter") and options for melee combat as well.');
INSERT INTO genre(Genre,Explanation) VALUES ('Vehicular Combat','Vehicular Combat games are a sub-genre of Combat games where characters control vehicles and use an arsenal of weapons and items, as well as speed, to destroy their opponents. These games may skew closer to realism or fantasy depending on the game, and may feature monster trucks, tanks, or armored cars, among others. Some Racing games have elements of Vehicular Combat but the primary goal is still to reach the finish line, not destroy your opponents.');
INSERT INTO genre(Genre,Explanation) VALUES ('Vehicular Sim','Vehicular Sims try to realistically simulate the experience of piloting a vehicle, whether this is a car, tank, train, plane, submarine, bus, spaceship, or other. These games are usually developed for niche hobbyist markets, as they lack mission objectives and focus on realism above all. They may also be used to actually train pilots in the operation of these vehicles.');
INSERT INTO genre(Genre,Explanation) VALUES ('Visual Novel','Visual Novels are a type of Adventure game that developed and became popular in Japan, especially the Japanese indie game scene. These games borrow their presentation from manga and anime, featuring text, still images, and character portraits. Like in traditional Adventure games there is no Action gameplay, only puzzles, conversations, and decisions for the character to make. These games continue to be very popular in Japan and have gained a foothold overseas as well.');
INSERT INTO genre(Genre,Explanation) VALUES ('Walking Simulator','This is a recent type of Adventure game that attempts to convey its story mostly through gameplay instead of cutscenes and exposition. Though the term was originally derisive, it has come to describe modern first-person and third-person Adventure games where the player simply explores, interacts, and listens as the story unfolds. The Silent Hills demo "P.T." showcased the genre''s potential for horror games, and many copycats have surfaced since.');
INSERT INTO genre(Genre,Explanation) VALUES ('WRPG','WRPGs (Western RPGs) is a term used to refer to the conventions of RPGs developed by American and Western European developers. Compared to their Eastern counterparts, WRPGs usually feature greater character customization, a more open game world, greater freedom in choosing your character''s path, some kind of morality system, and active (instead of turn-based) combat. Though consoles were previously dominated by JRPGs, WRPGs have made a huge comeback.');

INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (1,'Street Fighter III: 3rd Strike',1,'2D Fighting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (2,'Garou: Mark of the Wolves',1,'2D Fighting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (3,'Darkstalkers 3: Jedah''s Damnation',1,'2D Fighting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (4,'SoulCalibur II',2,'3D Fighting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (5,'Tekken 3',2,'3D Fighting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (6,'Virtua Fighter 4',2,'3D Fighting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (7,'Endless Legend',3,'4X Strategy');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (8,'Civilization V',3,'4X Strategy');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (9,'Sins of a Solar Empire',3,'4X Strategy');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (10,'Hagane: The Final Conflict',4,'Action');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (11,'Demon''s Crest',4,'Action');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (12,'Clash at Demonhead',4,'Action');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (13,'The Legend of Zelda: A Link to the Past',5,'Action-Adventure');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (14,'Legacy of Kain: Soul Reaver',5,'Action-Adventure');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (15,'Okami',5,'Action-Adventure');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (16,'Kingdom Hearts',6,'Action-RPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (17,'Dragon''s Dogma: Dark Arisen',6,'Action-RPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (18,'Dark Cloud 2',6,'Action-RPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (19,'Grim Fandango',7,'Adventure');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (20,'The Last Express',7,'Adventure');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (21,'The Secret of Monkey Island',7,'Adventure');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (22,'Ace Combat Zero: The Belkan War',8,'Air Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (23,'After Burner',8,'Air Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (24,'SkyGunner',8,'Air Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (25,'Super Smash Bros.',9,'Arena Brawler');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (26,'Power Stone 2',9,'Arena Brawler');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (27,'Jump Ultimate Stars',9,'Arena Brawler');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (28,'Pokken Tournament',10,'Arena Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (29,'Gundam Extreme Vs.',10,'Arena Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (30,'Dissidia: Final Fantasy',10,'Arena Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (31,'Unreal Tournament 3',11,'Arena Shooter');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (32,'Quake Arena',11,'Arena Shooter');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (33,'Team Fortress 2',11,'Arena Shooter');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (34,'Scorched Earth',12,'Artillery');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (35,'Worms: Armageddon',12,'Artillery');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (36,'Hogs of War',12,'Artillery');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (37,'Yume Nikki',13,'Atmospheric');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (38,'L.S.D: Dream Emulator',13,'Atmospheric');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (39,'Abzu',13,'Atmospheric');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (40,'Streets of Rage 2',14,'Beat-''em-Up');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (41,'Guardian Heroes',14,'Beat-''em-Up');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (42,'Castle Crashers',14,'Beat-''em-Up');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (43,'Mario Party 3',15,'Board Game');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (44,'Dokapon Kingdom',15,'Board Game');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (45,'Fortune Street',15,'Board Game');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (46,'Ikaruga',16,'Bullet Hell');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (47,'DoDonpachi Dai-Ou-Jou',16,'Bullet Hell');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (48,'Mushihime-sama',16,'Bullet Hell');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (49,'Uno',17,'Card Game');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (50,'Blackjack',17,'Card Game');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (51,'Texas Hold ''Em Poker',17,'Card Game');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (52,'Angry Birds',18,'Casual');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (53,'Flappy Bird',18,'Casual');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (54,'Fruit Ninja',18,'Casual');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (55,'Devil May Cry 3: Special Edition',19,'Character Action');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (56,'God Hand',19,'Character Action');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (57,'Bayonetta',19,'Character Action');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (58,'Jurassic Park: Operation Genesis',20,'CMS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (59,'Roller Coaster Tycoon',20,'CMS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (60,'Harvest Moon 64',20,'CMS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (61,'Super Mario 64',21,'Collect-a-thon Platform');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (62,'Spyro 2: Ripto''s Rage!',21,'Collect-a-thon Platform');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (63,'Banjo-Tooie',21,'Collect-a-thon Platform');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (64,'Twisted Metal 2',22,'Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (65,'Steel Battalion',22,'Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (66,'For Honor',22,'Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (67,'Tokimeki Memorial',23,'Dating Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (68,'Katawa Shoujo',23,'Dating Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (69,'Mystic Messenger',23,'Dating Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (70,'Wizardry: Proving Grounds of the Mad Overlord',24,'Dungeon Crawler');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (71,'Legend of Grimrock',24,'Dungeon Crawler');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (72,'Etrian Odyssey II',24,'Dungeon Crawler');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (73,'Oregon Trail',25,'Edutainment');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (74,'Brain Age',25,'Edutainment');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (75,'Donkey Kong Jr. Math',25,'Edutainment');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (76,'Temple Run 2',26,'Endless Runner');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (77,'Super Mario Run',26,'Endless Runner');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (78,'Canabalt',26,'Endless Runner');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (79,'Crimson Room',27,'Escape the Room');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (80,'DROOM',27,'Escape the Room');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (81,'999: Nine Hours,Nine Persons,Nine Doors',27,'Escape the Room');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (82,'Doom',28,'FPS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (83,'Call of Duty 4: Modern Warfare',28,'FPS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (84,'Half-Life',28,'FPS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (85,'Hearts of Iron',29,'Grand Strategy');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (86,'Crusader Kings',29,'Grand Strategy');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (87,'Romance of the Three Kingdoms XI',29,'Grand Strategy');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (88,'Diablo II',30,'Hack-n-Slash');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (89,'Dungeon Siege 2',30,'Hack-n-Slash');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (90,'Torchlight II',30,'Hack-n-Slash');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (91,'Cookie Clicker',31,'Incremental');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (92,'Clicker Heroes',31,'Incremental');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (93,'Sakura Clicker',31,'Incremental');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (94,'Photophobia',32,'Interactive Fiction');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (95,'Slouching Towards Bedlam',32,'Interactive Fiction');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (96,'A Mind Forever Voyaging',32,'Interactive Fiction');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (97,'Dragon''s Lair',33,'Interactive Movie');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (98,'Heavy Rain',33,'Interactive Movie');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (99,'Until Dawn',33,'Interactive Movie');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (100,'Final Fantasy VII',34,'JRPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (101,'Phantasy Star IV',34,'JRPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (102,'Shin Megami Tensei: Nocturne',34,'JRPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (103,'Mario Kart 64',35,'Kart Racing');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (104,'Crash Team Racing',35,'Kart Racing');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (105,'ModNation Racers',35,'Kart Racing');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (106,'Zero Wing',36,'Kusoge');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (107,'Chou Aniki',36,'Kusoge');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (108,'Zelda''s Adventure',36,'Kusoge');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (109,'Animal Crossing',37,'Life Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (110,'The Sims',37,'Life Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (111,'Tomodachi Life',37,'Life Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (112,'House of the Dead 2',38,'Light Gun');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (113,'Time Crisis II',38,'Light Gun');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (114,'Virtua Cop',38,'Light Gun');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (115,'Tetris',39,'Matching Puzzle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (116,'Puyo Puyo Fever',39,'Matching Puzzle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (117,'Magical Drop F',39,'Matching Puzzle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (118,'Pac-Man',40,'Maze');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (119,'Zombies Ate My Neighbors!',40,'Maze');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (120,'Hotline Miami',40,'Maze');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (121,'Armored Core 2',41,'Mech Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (122,'Another Century''s Episode 3',41,'Mech Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (123,'Zone of the Enders: The 2nd Runner',41,'Mech Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (124,'Cave Story',42,'Metroidvania');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (125,'Axiom Verge',42,'Metroidvania');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (126,'Guacamelee',42,'Metroidvania');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (127,'Arma III',43,'Military Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (128,'America''s Army',43,'Military Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (129,'Project Reality',43,'Military Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (130,'Incredible Crisis',44,'Mini-games');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (131,'WarioWare D.I.Y.',44,'Mini-games');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (132,'Rhythm Heaven',44,'Mini-games');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (133,'World of Warcraft',45,'MMO');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (134,'EVE Online',45,'MMO');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (135,'War Thunder',45,'MMO');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (136,'DOTA 2',46,'MOBA');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (137,'League of Legends',46,'MOBA');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (138,'Smite',46,'MOBA');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (139,'PokEmon Gold/Silver',47,'Monster Collecting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (140,'Dragon Warrior Monsters 2',47,'Monster Collecting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (141,'Monster Rancher 2',47,'Monster Collecting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (142,'Dynasty Warriors 4',48,'Musou');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (143,'Warriors Orochi Z',48,'Musou');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (144,'Sengoku Basara 4: UTAGE',48,'Musou');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (145,'Grand Theft Auto: Vice City',49,'Open World');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (146,'The Witcher: Wild Hunt',49,'Open World');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (147,'The Elder Scrolls IV: Oblivion',49,'Open World');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (148,'Pong',50,'Paddle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (149,'Breakout',50,'Paddle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (150,'FlingSmash',50,'Paddle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (151,'Wii Party',51,'Party');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (152,'TV Show King Party',51,'Party');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (153,'Viva Pinata: Party Animals',51,'Party');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (154,'Portal 2',52,'Physics Puzzle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (155,'World of Goo',52,'Physics Puzzle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (156,'Kerbal Space Program',52,'Physics Puzzle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (157,'DuckTales',53,'Platform');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (158,'Super Mario World',53,'Platform');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (159,'Spelunky',53,'Platform');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (160,'Fire Pro Wrestling Returns',54,'Pro Wrestling');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (161,'WWE SmackDown!',54,'Pro Wrestling');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (162,'Virtual Pro Wrestling 2',54,'Pro Wrestling');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (163,'Carnage Heart',55,'Programming');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (164,'Hackmud',55,'Programming');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (165,'Pony Island',55,'Programming');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (166,'Picross 3D',56,'Puzzle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (167,'Echochrome',56,'Puzzle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (168,'Monument Valley',56,'Puzzle');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (169,'OutRun',57,'Racing');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (170,'F-Zero GX',57,'Racing');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (171,'Gran Turismo 7',57,'Racing');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (172,'Sin & Punishment: Successor of the Earth',58,'Rail Shooter');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (173,'StarFox 64',58,'Rail Shooter');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (174,'Rez',58,'Rail Shooter');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (175,'Nintendogs',59,'Raising Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (176,'Seaman',59,'Raising Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (177,'Princess Maker 2',59,'Raising Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (178,'Elite Beat Agents',60,'Rhythm');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (179,'Donkey Konga 2',60,'Rhythm');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (180,'Gitaroo Man',60,'Rhythm');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (181,'Pokemon Mystery Dungeon',61,'Roguelike');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (182,'FTL: Faster Than Light',61,'Roguelike');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (183,'The Binding of Isaac',61,'Roguelike');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (184,'Wasteland',62,'RPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (185,'Arcanum: Of Steamworks and Magick Obscura',62,'RPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (186,'Dragon Quest VIII: Journey of the Cursed King',62,'RPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (187,'StarCraft II: Wings of Liberty',63,'RTS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (188,'Age of Empires II: Age of Kings',63,'RTS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (189,'Warcraft III: Reign of Chaos',63,'RTS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (190,'Gunstar Heroes',64,'Run-n-Gun');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (191,'Metal Slug 3',64,'Run-n-Gun');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (192,'Contra III: The Alien Wars',64,'Run-n-Gun');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (193,'Einhander',65,'Shoot ''em Up');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (194,'Radiant Silvergun',65,'Shoot ''em Up');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (195,'Soukyuu Gurentai',65,'Shoot ''em Up');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (196,'Mega Man 2',66,'Shooting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (197,'Space Harrier',66,'Shooting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (198,'Fantasy Zone',66,'Shooting');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (199,'The Sims 3',67,'Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (200,'FlightGear',67,'Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (201,'Animal Crossing',67,'Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (202,'Tony Hawk''s Pro Skater 2',68,'Sports');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (203,'NBA Jam',68,'Sports');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (204,'Super Punch-Out!!',68,'Sports');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (205,'Fire Emblem: Genealogy of the Holy War',69,'SRPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (206,'Final Fantasy Tactics',69,'SRPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (207,'XCOM: Enemy Unknown',69,'SRPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (208,'Metal Gear Solid 3: Subsistence',70,'Stealth');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (209,'Thief',70,'Stealth');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (210,'Hitman: Blood Money',70,'Stealth');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (211,'King of Dragon Pass',71,'Strategy');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (212,'Jagged Alliance 2',71,'Strategy');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (213,'Invisible,Inc.',71,'Strategy');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (214,'This War of Mine',72,'Survival');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (215,'Don''t Starve',72,'Survival');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (216,'Ark: Survival Evolved',72,'Survival');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (217,'Resident Evil',73,'Survival Horror');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (218,'Silent Hill 2',73,'Survival Horror');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (219,'Fatal Frame II: Crimson Butterfly',73,'Survival Horror');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (220,'Heroes of Might and Magic III',74,'TBS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (221,'Advance Wars 2: Black Hole Rising',74,'TBS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (222,'Total War: Rome II',74,'TBS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (223,'Yu-Gi-Oh! The Duelists of the Roses',75,'TCG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (224,'Pokemon Trading Card Game',75,'TCG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (225,'Hearthstone: Heroes of Warcraft',75,'TCG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (226,'Diner Dash',76,'Time Management');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (227,'Airport Mania',76,'Time Management');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (228,'Ore No Ryouri',76,'Time Management');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (229,'Rampart',77,'Tower Defense');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (230,'Lock''s Quest',77,'Tower Defense');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (231,'Plants vs. Zombies',77,'Tower Defense');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (232,'Vanquish',78,'TPS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (233,'Max Payne',78,'TPS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (234,'Uncharted 2: Among Thieves',78,'TPS');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (235,'Carmageddon',79,'Vehicular Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (236,'Vigilante 8',79,'Vehicular Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (237,'Twisted Metal: Black',79,'Vehicular Combat');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (238,'Assetto Corza',80,'Vehicular Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (239,'X-Plane 10',80,'Vehicular Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (240,'Southern Belle',80,'Vehicular Sim');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (241,'Fate/stay Night',81,'Visual Novel');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (242,'Steins',81,'Visual Novel');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (243,'Gate',81,'Visual Novel');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (244,'Gone Home',82,'Walking Simulator');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (245,'Dear Esther',82,'Walking Simulator');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (246,'Firewatch',82,'Walking Simulator');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (247,'Planescape: Torment',83,'WRPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (248,'Fallout: New Vegas',83,'WRPG');
INSERT INTO game(GameID,Game,GenreID,Genre) VALUES (249,'Mass Effect 2',83,'WRPG');


