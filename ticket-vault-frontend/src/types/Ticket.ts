
export interface TicketComplete {
    issueKey: string,
    summary: string,
    description: string,

    createdAt: Date,
    resolvedAt: Date,

    issueType: string,
    resolution: string,
    status: string,

    creator: string,
    reporter: string,
    project: string,
    watchers: string[],
    labels: string[],
    comments: Comment[],
    attachments: Attachment[],
    archivedTicketId: string,

    customValues: { [key: string]: string },
}

export interface Comment {
    author: string,
    content: string,
    createdAt: Date,
}

export interface Attachment {
    name: string,
    url: string,
    uploader: string,
    createdAt: Date,
}