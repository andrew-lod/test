/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     03/06/2024 10:50:23                          */
/*==============================================================*/

/*==============================================================*/
/* Domain: EXPLANATION                                          */
/*==============================================================*/
create domain EXPLANATION as VARCHAR(128);

/*==============================================================*/
/* Domain: INFORMATION                                          */
/*==============================================================*/
create domain INFORMATION as VARCHAR(512);

/*==============================================================*/
/* Domain: KEY                                                  */
/*==============================================================*/
create domain KEY as VARCHAR(30);

/*==============================================================*/
/* Domain: MEDIATYPE                                            */
/*==============================================================*/
create domain MEDIATYPE as VARCHAR(30);

/*==============================================================*/
/* Domain: NAME                                                 */
/*==============================================================*/
create domain NAME as VARCHAR(256);

/*==============================================================*/
/* Domain: PARAGRAPH                                            */
/*==============================================================*/
create domain PARAGRAPH as TEXT;

/*==============================================================*/
/* Domain: PROJECTTYPE                                          */
/*==============================================================*/
create domain PROJECTTYPE as VARCHAR(25);

/*==============================================================*/
/* Domain: PROJETTYPE                                           */
/*==============================================================*/
create domain PROJETTYPE as VARCHAR(25);

/*==============================================================*/
/* Domain: REFERENCEID                                          */
/*==============================================================*/
create domain REFERENCEID as INT;

/*==============================================================*/
/* Domain: "TIMESTAMP"                                          */
/*==============================================================*/
create domain "TIMESTAMP" as DATE;

/*==============================================================*/
/* Domain: URL                                                  */
/*==============================================================*/
create domain URL as VARCHAR(512);

/*==============================================================*/
/* Table: ARCHIVEDOBJECT                                        */
/*==============================================================*/
create table ARCHIVEDOBJECT (
   ID                   Serial                   not null,
   OBJECT               PARAGRAPH            not null,
   MEDIA_TYPE           MEDIATYPE            not null,
   constraint PK_ARCHIVEDOBJECT primary key (ID)
);

/*==============================================================*/
/* Index: ARCHIVEDOBJECT_PK                                     */
/*==============================================================*/
create unique index ARCHIVEDOBJECT_PK on ARCHIVEDOBJECT (
ID
);

/*==============================================================*/
/* Table: ARCHIVEDOBJECTMAPPING                                 */
/*==============================================================*/
create table ARCHIVEDOBJECTMAPPING (
   OBJECT_ID            REFERENCEID          not null,
   ROUTE_ID             REFERENCEID          not null,
   KEY                  KEY                  not null,
   constraint PK_ARCHIVEDOBJECTMAPPING primary key (OBJECT_ID, ROUTE_ID, KEY)
);

/*==============================================================*/
/* Index: ARCHIVEDOBJECTMAPPING_PK                              */
/*==============================================================*/
create unique index ARCHIVEDOBJECTMAPPING_PK on ARCHIVEDOBJECTMAPPING (
OBJECT_ID,
ROUTE_ID,
KEY
);

/*==============================================================*/
/* Index: MAPPING_ON_ARCHIVEDOBJECT_FK                          */
/*==============================================================*/
create  index MAPPING_ON_ARCHIVEDOBJECT_FK on ARCHIVEDOBJECTMAPPING (
OBJECT_ID
);

/*==============================================================*/
/* Index: ROUTE_OF_MAPPING2_FK                                  */
/*==============================================================*/
create  index ROUTE_OF_MAPPING2_FK on ARCHIVEDOBJECTMAPPING (
ROUTE_ID
);

/*==============================================================*/
/* Table: ATTACHMENT                                            */
/*==============================================================*/
create table ATTACHMENT (
   ISSUE_KEY            KEY                  not null,
   USERNAME             NAME                 not null,
   NAME                 NAME                 not null,
   UPLOAD_DATE          "TIMESTAMP"          not null,
   URL                  URL                  not null,
   constraint PK_ATTACHMENT primary key (ISSUE_KEY, USERNAME, NAME, UPLOAD_DATE)
);

/*==============================================================*/
/* Index: ATTACHMENT_PK                                         */
/*==============================================================*/
create unique index ATTACHMENT_PK on ATTACHMENT (
ISSUE_KEY,
USERNAME,
NAME,
UPLOAD_DATE
);

/*==============================================================*/
/* Index: ATTACHMENT_ON_TICKET_FK                               */
/*==============================================================*/
create  index ATTACHMENT_ON_TICKET_FK on ATTACHMENT (
ISSUE_KEY
);

/*==============================================================*/
/* Index: UPLOADER_OF_ATTACHMENT_FK                             */
/*==============================================================*/
create  index UPLOADER_OF_ATTACHMENT_FK on ATTACHMENT (
USERNAME
);

/*==============================================================*/
/* Table: COMMENT                                               */
/*==============================================================*/
create table COMMENT (
   ISSUE_KEY            KEY                  not null,
   USERNAME             NAME                 not null,
   COMMENT              PARAGRAPH            not null,
   COMMENT_DATE         "TIMESTAMP"          not null,
   constraint PK_COMMENT primary key (ISSUE_KEY, USERNAME, COMMENT, COMMENT_DATE)
);

/*==============================================================*/
/* Index: COMMENT_PK                                            */
/*==============================================================*/
create unique index COMMENT_PK on COMMENT (
ISSUE_KEY,
USERNAME,
COMMENT,
COMMENT_DATE
);

/*==============================================================*/
/* Index: COMMENT_ON_TICKET_FK                                  */
/*==============================================================*/
create  index COMMENT_ON_TICKET_FK on COMMENT (
ISSUE_KEY
);

/*==============================================================*/
/* Index: COMMENTER_OF_COMMENT_FK                               */
/*==============================================================*/
create  index COMMENTER_OF_COMMENT_FK on COMMENT (
USERNAME
);

/*==============================================================*/
/* Table: CUSTOMATTRIBUTE                                       */
/*==============================================================*/
create table CUSTOMATTRIBUTE (
   KEY                  KEY                  not null,
   constraint PK_CUSTOMATTRIBUTE primary key (KEY)
);

/*==============================================================*/
/* Index: CUSTOMATTRIBUTE_PK                                    */
/*==============================================================*/
create unique index CUSTOMATTRIBUTE_PK on CUSTOMATTRIBUTE (
KEY
);

/*==============================================================*/
/* Table: CUSTOMATTRIBUTEMAPPING                                */
/*==============================================================*/
create table CUSTOMATTRIBUTEMAPPING (
   CUSTOM_ATTRIBUTE_KEY KEY                  not null,
   MAPPING_ID           REFERENCEID          not null,
   constraint PK_CUSTOMATTRIBUTEMAPPING primary key (CUSTOM_ATTRIBUTE_KEY, MAPPING_ID)
);

/*==============================================================*/
/* Index: CUSTOMATTRIBUTEMAPPING_PK                             */
/*==============================================================*/
create unique index CUSTOMATTRIBUTEMAPPING_PK on CUSTOMATTRIBUTEMAPPING (
CUSTOM_ATTRIBUTE_KEY,
MAPPING_ID
);

/*==============================================================*/
/* Index: CUSTOMATTRIBUTE_ON_MAPPING_FK                         */
/*==============================================================*/
create  index CUSTOMATTRIBUTE_ON_MAPPING_FK on CUSTOMATTRIBUTEMAPPING (
CUSTOM_ATTRIBUTE_KEY
);

/*==============================================================*/
/* Index: ROUTE_OF_MAPPING_FK2                                  */
/*==============================================================*/
create  index ROUTE_OF_MAPPING_FK2 on CUSTOMATTRIBUTEMAPPING (
MAPPING_ID
);

/*==============================================================*/
/* Table: CUSTOMVALUE                                           */
/*==============================================================*/
create table CUSTOMVALUE (
   KEY                  KEY                  not null,
   ISSUE_KEY            KEY                  not null,
   VALUE                PARAGRAPH            not null,
   constraint PK_CUSTOMVALUE primary key (KEY, ISSUE_KEY)
);

/*==============================================================*/
/* Index: CUSTOMVALUE_PK                                        */
/*==============================================================*/
create unique index CUSTOMVALUE_PK on CUSTOMVALUE (
KEY,
ISSUE_KEY
);

/*==============================================================*/
/* Index: VALUE_ON_ATTRIBUTE_FK                                 */
/*==============================================================*/
create  index VALUE_ON_ATTRIBUTE_FK on CUSTOMVALUE (
KEY
);

/*==============================================================*/
/* Index: CUSTOMVALUE_ON_TICKET_FK                              */
/*==============================================================*/
create  index CUSTOMVALUE_ON_TICKET_FK on CUSTOMVALUE (
ISSUE_KEY
);

/*==============================================================*/
/* Table: FILTER                                                */
/*==============================================================*/
create table FILTER (
   ID                   Serial                   not null,
   NAME                 NAME                 not null,
   QUERY                INFORMATION          not null,
   constraint PK_FILTER primary key (ID)
);

/*==============================================================*/
/* Index: FILTER_PK                                             */
/*==============================================================*/
create unique index FILTER_PK on FILTER (
ID
);

/*==============================================================*/
/* Table: GLOBALMAPPING                                         */
/*==============================================================*/
create table GLOBALMAPPING (
   ROUTE_ID             REFERENCEID          not null,
   KEY                  KEY                  not null,
   constraint PK_GLOBALMAPPING primary key (ROUTE_ID, KEY)
);

/*==============================================================*/
/* Index: GLOBALMAPPING_PK                                      */
/*==============================================================*/
create unique index GLOBALMAPPING_PK on GLOBALMAPPING (
ROUTE_ID,
KEY
);

/*==============================================================*/
/* Index: ROUTE_OF_MAPPING_FK                                   */
/*==============================================================*/
create  index ROUTE_OF_MAPPING_FK on GLOBALMAPPING (
ROUTE_ID
);

/*==============================================================*/
/* Table: ISSUETYPE                                             */
/*==============================================================*/
create table ISSUETYPE (
   NAME                 NAME                 not null,
   constraint PK_ISSUETYPE primary key (NAME)
);

/*==============================================================*/
/* Index: ISSUETYPE_PK                                          */
/*==============================================================*/
create unique index ISSUETYPE_PK on ISSUETYPE (
NAME
);

/*==============================================================*/
/* Table: LABEL                                                 */
/*==============================================================*/
create table LABEL (
   NAME                 NAME                 not null,
   constraint PK_LABEL primary key (NAME)
);

/*==============================================================*/
/* Index: LABEL_PK                                              */
/*==============================================================*/
create unique index LABEL_PK on LABEL (
NAME
);

/*==============================================================*/
/* Table: LABELS_IN_TICKET                                      */
/*==============================================================*/
create table LABELS_IN_TICKET (
   NAME                 NAME                 not null,
   ISSUE_KEY            KEY                  not null,
   constraint PK_LABELS_IN_TICKET primary key (NAME, ISSUE_KEY)
);

/*==============================================================*/
/* Index: LABELS_IN_TICKET_PK                                   */
/*==============================================================*/
create unique index LABELS_IN_TICKET_PK on LABELS_IN_TICKET (
NAME,
ISSUE_KEY
);

/*==============================================================*/
/* Index: LABELS_IN_TICKET_FK                                   */
/*==============================================================*/
create  index LABELS_IN_TICKET_FK on LABELS_IN_TICKET (
NAME
);

/*==============================================================*/
/* Index: LABELS_IN_TICKET2_FK                                  */
/*==============================================================*/
create  index LABELS_IN_TICKET2_FK on LABELS_IN_TICKET (
ISSUE_KEY
);

/*==============================================================*/
/* Table: PIN                                                   */
/*==============================================================*/
create table PIN (
   ISSUE_KEY            KEY                  not null,
   constraint PK_PIN primary key (ISSUE_KEY)
);

/*==============================================================*/
/* Index: PIN_PK                                                */
/*==============================================================*/
create unique index PIN_PK on PIN (
ISSUE_KEY
);

/*==============================================================*/
/* Table: PROJECT                                               */
/*==============================================================*/
create table PROJECT (
   KEY                  KEY                  not null,
   LEAD                 NAME                 null,
   NAME                 NAME                 not null,
   TYPE                 PROJETTYPE           null,
   DESCRIPTION          PARAGRAPH            null,
   URL                  URL                  null,
   constraint PK_PROJECT primary key (KEY)
);

/*==============================================================*/
/* Index: PROJECT_PK                                            */
/*==============================================================*/
create unique index PROJECT_PK on PROJECT (
KEY
);

/*==============================================================*/
/* Index: LEAD_OF_PROJECT_FK                                    */
/*==============================================================*/
create  index LEAD_OF_PROJECT_FK on PROJECT (
LEAD
);

/*==============================================================*/
/* Table: RESOLUTION                                            */
/*==============================================================*/
create table RESOLUTION (
   NAME                 NAME                 not null,
   constraint PK_RESOLUTION primary key (NAME)
);

/*==============================================================*/
/* Index: RESOLUTION_PK                                         */
/*==============================================================*/
create unique index RESOLUTION_PK on RESOLUTION (
NAME
);

/*==============================================================*/
/* Table: ROUTE                                                 */
/*==============================================================*/
create table ROUTE (
   ID                   Serial                   not null,
   PARENT_ROUTE         REFERENCEID          null,
   ROUTE                PARAGRAPH            null,
   KEY_PROCESSING       EXPLANATION          null,
   VALUE_PROCESSING     EXPLANATION          null,
   constraint PK_ROUTE primary key (ID)
);

/*==============================================================*/
/* Index: ROUTE_PK                                              */
/*==============================================================*/
create unique index ROUTE_PK on ROUTE (
ID
);

/*==============================================================*/
/* Index: PARENT_OF_ROUTE_FK                                    */
/*==============================================================*/
create  index PARENT_OF_ROUTE_FK on ROUTE (
PARENT_ROUTE
);

/*==============================================================*/
/* Table: STATUS                                                */
/*==============================================================*/
create table STATUS (
   NAME                 NAME                 not null,
   constraint PK_STATUS primary key (NAME)
);

/*==============================================================*/
/* Index: STATUS_PK                                             */
/*==============================================================*/
create unique index STATUS_PK on STATUS (
NAME
);

/*==============================================================*/
/* Table: TICKET                                                */
/*==============================================================*/
create table TICKET (
   ISSUE_KEY            KEY                  not null,
   PROJECT_KEY          KEY                  null,
   SUMMARY              INFORMATION          null,
   DESCRIPTION          PARAGRAPH            null,
   ISSUE_TYPE           NAME                 null,
   STATUS               NAME                 null,
   RESOLUTION           NAME                 null,
   REPORTER             NAME                 null,
   ASSIGNEE             NAME                 null,
   CREATOR              NAME                 null,
   CREATED_DATE         "TIMESTAMP"          null,
   RESOLVED_DATE        "TIMESTAMP"          null,
   OBJECT_ID            REFERENCEID          not null,
   constraint PK_TICKET primary key (ISSUE_KEY)
);

/*==============================================================*/
/* Index: TICKET_PK                                             */
/*==============================================================*/
create unique index TICKET_PK on TICKET (
ISSUE_KEY
);

/*==============================================================*/
/* Index: ISSUETYPE_OF_TICKET_FK                                */
/*==============================================================*/
create  index ISSUETYPE_OF_TICKET_FK on TICKET (
ISSUE_TYPE
);

/*==============================================================*/
/* Index: RESOLUTION_OF_TICKET_FK                               */
/*==============================================================*/
create  index RESOLUTION_OF_TICKET_FK on TICKET (
RESOLUTION
);

/*==============================================================*/
/* Index: STATUS_OF_TICKET_FK                                   */
/*==============================================================*/
create  index STATUS_OF_TICKET_FK on TICKET (
STATUS
);

/*==============================================================*/
/* Index: CREATOR_OF_TICKET_FK                                  */
/*==============================================================*/
create  index CREATOR_OF_TICKET_FK on TICKET (
CREATOR
);

/*==============================================================*/
/* Index: REPORTER_OF_TICKET_FK                                 */
/*==============================================================*/
create  index REPORTER_OF_TICKET_FK on TICKET (
REPORTER
);

/*==============================================================*/
/* Index: ASSIGNEE_OF_TICKET_FK                                 */
/*==============================================================*/
create  index ASSIGNEE_OF_TICKET_FK on TICKET (
ASSIGNEE
);

/*==============================================================*/
/* Index: PROJECT_OF_TICKET_FK                                  */
/*==============================================================*/
create  index PROJECT_OF_TICKET_FK on TICKET (
PROJECT_KEY
);

/*==============================================================*/
/* Index: ARCHIVEDOBJECT_OF_TICKET_FK                           */
/*==============================================================*/
create  index ARCHIVEDOBJECT_OF_TICKET_FK on TICKET (
OBJECT_ID
);

/*==============================================================*/
/* Table: USERREFERENCE                                         */
/*==============================================================*/
create table USERREFERENCE (
   USERNAME             NAME                 not null,
   constraint PK_USERREFERENCE primary key (USERNAME)
);

/*==============================================================*/
/* Index: USERREFERENCE_PK                                      */
/*==============================================================*/
create unique index USERREFERENCE_PK on USERREFERENCE (
USERNAME
);

/*==============================================================*/
/* Table: WATCHERS_OF_TICKET                                    */
/*==============================================================*/
create table WATCHERS_OF_TICKET (
   USERNAME             NAME                 not null,
   ISSUE_KEY            KEY                  not null,
   constraint PK_WATCHERS_OF_TICKET primary key (USERNAME, ISSUE_KEY)
);

/*==============================================================*/
/* Index: WATCHERS_IN_TICKET_PK                                 */
/*==============================================================*/
create unique index WATCHERS_IN_TICKET_PK on WATCHERS_OF_TICKET (
USERNAME,
ISSUE_KEY
);

/*==============================================================*/
/* Index: WATCHERS_IN_TICKET_FK                                 */
/*==============================================================*/
create  index WATCHERS_IN_TICKET_FK on WATCHERS_OF_TICKET (
USERNAME
);

/*==============================================================*/
/* Index: WATCHERS_IN_TICKET2_FK                                */
/*==============================================================*/
create  index WATCHERS_IN_TICKET2_FK on WATCHERS_OF_TICKET (
ISSUE_KEY
);

alter table ARCHIVEDOBJECTMAPPING
   add constraint FK_ARCHIVED_MAPPING_O_ARCHIVED foreign key (OBJECT_ID)
      references ARCHIVEDOBJECT (ID)
      on delete restrict on update restrict;

alter table ARCHIVEDOBJECTMAPPING
   add constraint FK_ARCHIVED_ROUTE_OF__ROUTE foreign key (ROUTE_ID)
      references ROUTE (ID)
      on delete restrict on update restrict;

alter table ATTACHMENT
   add constraint FK_ATTACHME_ATTACHMEN_TICKET foreign key (ISSUE_KEY)
      references TICKET (ISSUE_KEY)
      on delete restrict on update restrict;

alter table ATTACHMENT
   add constraint FK_ATTACHME_UPLOADER__USERREFE foreign key (USERNAME)
      references USERREFERENCE (USERNAME)
      on delete restrict on update restrict;

alter table COMMENT
   add constraint FK_COMMENT_COMMENTER_USERREFE foreign key (USERNAME)
      references USERREFERENCE (USERNAME)
      on delete restrict on update restrict;

alter table COMMENT
   add constraint FK_COMMENT_COMMENT_O_TICKET foreign key (ISSUE_KEY)
      references TICKET (ISSUE_KEY)
      on delete restrict on update restrict;

alter table CUSTOMATTRIBUTEMAPPING
   add constraint FK_CUSTOMAT_CUSTOMATT_CUSTOMAT foreign key (CUSTOM_ATTRIBUTE_KEY)
      references CUSTOMATTRIBUTE (KEY)
      on delete cascade on update restrict;

alter table CUSTOMATTRIBUTEMAPPING
   add constraint FK_CUSTOMAT_ROUTE_OF__ROUTE foreign key (MAPPING_ID)
      references ROUTE (ID)
      on delete restrict on update restrict;

alter table CUSTOMVALUE
   add constraint FK_CUSTOMVA_CUSTOMVAL_TICKET foreign key (ISSUE_KEY)
      references TICKET (ISSUE_KEY)
      on delete restrict on update restrict;

alter table CUSTOMVALUE
   add constraint FK_CVALUE_REF_CATTRIBUTE foreign key (KEY)
      references CUSTOMATTRIBUTE (KEY)
      on delete cascade on update restrict;

alter table GLOBALMAPPING
   add constraint FK_GLOBALMA_ROUTE_OF__ROUTE foreign key (ROUTE_ID)
      references ROUTE (ID)
      on delete restrict on update restrict;

alter table LABELS_IN_TICKET
   add constraint FK_LABELS_I_LABELS_IN_LABEL foreign key (NAME)
      references LABEL (NAME)
      on delete restrict on update restrict;

alter table LABELS_IN_TICKET
   add constraint FK_LABELS_I_LABELS_IN_TICKET foreign key (ISSUE_KEY)
      references TICKET (ISSUE_KEY)
      on delete restrict on update restrict;

alter table PIN
   add constraint FK_PIN_PIN_ON_TI_TICKET foreign key (ISSUE_KEY)
      references TICKET (ISSUE_KEY)
      on delete restrict on update restrict;

alter table PROJECT
   add constraint FK_PROJECT_LEAD_OF_P_USERREFE foreign key (LEAD)
      references USERREFERENCE (USERNAME)
      on delete restrict on update restrict;

alter table ROUTE
   add constraint FK_ROUTE_PARENT_OF_ROUTE foreign key (PARENT_ROUTE)
      references ROUTE (ID)
      on delete restrict on update restrict;

alter table TICKET
   add constraint FK_TICKET_ARCHIVEDO_ARCHIVED foreign key (OBJECT_ID)
      references ARCHIVEDOBJECT (ID)
      on delete restrict on update restrict;

alter table TICKET
   add constraint FK_TICKET_ASSIGNEE__USERREFE foreign key (ASSIGNEE)
      references USERREFERENCE (USERNAME)
      on delete restrict on update restrict;

alter table TICKET
   add constraint FK_TICKET_CREATOR_O_USERREFE foreign key (CREATOR)
      references USERREFERENCE (USERNAME)
      on delete restrict on update restrict;

alter table TICKET
   add constraint FK_TICKET_ISSUETYPE_ISSUETYP foreign key (ISSUE_TYPE)
      references ISSUETYPE (NAME)
      on delete restrict on update restrict;

alter table TICKET
   add constraint FK_TICKET_PROJECT_O_PROJECT foreign key (PROJECT_KEY)
      references PROJECT (KEY)
      on delete restrict on update restrict;

alter table TICKET
   add constraint FK_TICKET_REPORTER__USERREFE foreign key (REPORTER)
      references USERREFERENCE (USERNAME)
      on delete restrict on update restrict;

alter table TICKET
   add constraint FK_TICKET_RESOLUTIO_RESOLUTI foreign key (RESOLUTION)
      references RESOLUTION (NAME)
      on delete restrict on update restrict;

alter table TICKET
   add constraint FK_TICKET_STATUS_OF_STATUS foreign key (STATUS)
      references STATUS (NAME)
      on delete restrict on update restrict;

alter table WATCHERS_OF_TICKET
   add constraint FK_WATCHERS_WATCHERS__USERREFE foreign key (USERNAME)
      references USERREFERENCE (USERNAME)
      on delete restrict on update restrict;

alter table WATCHERS_OF_TICKET
   add constraint FK_WATCHERS_WATCHERS__TICKET foreign key (ISSUE_KEY)
      references TICKET (ISSUE_KEY)
      on delete restrict on update restrict;

