export interface PagedTickets {
    content: TicketOverview[],
    totalElements: number,
    currentPage: number,
    totalPages: number,
}

export interface TicketOverview {
    issueKey: string,
    summary: string,
}

export interface SearchFields {
    projects: string[],
    issueTypes: string[],
    resolutions: string[],
    statuses: string[],
    labels: string[],
    users: string[]
}

export interface SelectedFields {
    project?: string
    issueType?: string,
    resolution?: string,
    status?: string,
    labels?: string,
}  