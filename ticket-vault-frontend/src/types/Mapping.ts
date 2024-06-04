export interface CustomValueMapping {
    keyName: string,
    route: Route | null
}

export interface Route {
    path: string,
    next: Route | null,
    keyProcessing: string | null,
    valueProcessing: string | null
}