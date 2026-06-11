import axios from "axios";
import { AUTH_URL } from "./config";

export const authClient = axios.create({
  baseURL: AUTH_URL,
  headers: {
    "Content-type": "application/json",
  },
});
