export interface AuthRequest {
  username: string;
  password: string;
}
export interface AuthResponse {
  token: string;
}

export interface TaskResponseDto {
  id: string;
  title: string;
  description: string;
  isCompleted: boolean;
  createdAt: string;
}

export interface TaskRequestDto {
  title: string;
  description: string;
}

export interface TaskUpdateDto {
  isCompleted: boolean;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: { [key: string]: string[] };
}
