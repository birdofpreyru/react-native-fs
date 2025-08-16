export interface ErrorStatus {
  type: 'error';
  message?: string;
}
export interface SuccessStatus {
  type: 'success';
  message?: string;
}
export interface PendingStatus {
  type: 'pending';
}
export interface NotAvailableStatus {
  type: 'notAvailable';
  message: string;
}

export type Status =
  | ErrorStatus
  | SuccessStatus
  | PendingStatus
  | NotAvailableStatus;

export type StatusOrEvaluator =
  | Status
  | (() => Status)
  | (() => Promise<Status>);

export type TestMethods = { [name: string]: StatusOrEvaluator };
