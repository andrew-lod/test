DELETE FROM globalmapping;
DELETE FROM route;


INSERT INTO route (parent_route, route, key_processing, value_processing)
VALUES (NULL, 'Issue key', NULL, NULL),
       (NULL, 'Summary', NULL, NULL),
       (NULL, 'Description', NULL, NULL),

        (NULL, 'Created', NULL, NULL),
        (NULL, 'Resolved', NULL, NULL),

        (NULL, 'Issue Type', NULL, NULL),
        (NULL, 'Resolution', NULL, NULL),
        (NULL, 'Status', NULL, NULL),

        (NULL, 'Creator', NULL, NULL),
        (NULL, 'Reporter', NULL, NULL),
        (NULL, 'Assignee', NULL, NULL),

        (NULL, 'Watchers', '1', NULL),
        (NULL, NULL, NULL, NULL),

        (NULL, 'Labels', '1', NULL),
        (NULL, NULL, NULL, NULL),

        (NULL, 'Comment', '1', NULL),
        (NULL, NULL, NULL, '^[^;]+;\s*([^;]+);\s*.*$'),
        (NULL, NULL, NULL, '^.*?;\s*[^;]+;\s*(.*)'),
        (NULL, NULL, NULL, '^([^;]+);\s*[^;]+;\s*.*$'),

        (NULL, 'Attachment', '1', NULL),
        (NULL, NULL, NULL, '^[^;]+;\s*[^;]+;\s*([^;]+)'),
        (NULL, NULL, NULL, '^[^;]+;\s*[^;]+;\s*[^;]+;\s*(.*)$'),
        (NULL, NULL, NULL, '^[^;]+;\s*([^;]+);\s*[^;]+;\s*.*$'),
        (NULL, NULL, NULL, '^([^;]+);\s*[^;]+;\s*[^;]+;\s*.*$'),

        (NULL, 'Project key', NULL, NULL),
        (NULL, 'Project name', NULL, NULL),
        (NULL, 'Project lead', NULL, NULL),
        (NULL, 'Project type', NULL, NULL),
        (NULL, 'Project description', NULL, NULL),
        (NULL, 'Project url', NULL, NULL)
;

INSERT INTO globalmapping (key, route_id)
VALUES  ('ticket_issue_key', 1),
        ('ticket_summary', 2),
        ('ticket_description', 3),

        ('ticket_created_at', 4),
        ('ticket_resolved_at', 5),

        ('ticket_issue_type', 6),
        ('ticket_resolution', 7),
        ('ticket_status', 8),

        ('ticket_creator', 9),
        ('ticket_reporter', 10),
        ('ticket_assignee', 11),

        ('ticket_watchers', 12),
        ('watcher_name', 13),

        ('ticket_labels', 14),
        ('label_name', 15),

        ('ticket_comments', 16),
        ('comment_author', 17),
        ('comment_body', 18),
        ('comment_created_at', 19),

        ('ticket_attachments', 20),
        ('attachment_name', 21),
        ('attachment_url', 22),
        ('attachment_uploader', 23),
        ('attachment_uploaded_at', 24),

        ('project_key', 25),
        ('project_name', 26),
        ('project_lead', 27),
        ('project_type', 28),
        ('project_description', 29),
        ('project_url', 30)
    ;