import axios from "axios";

export default axios.create({
  baseURL: process.env.REACT_APP_API_URL || "http://localhost:8080/take",
  httpsAgent: null,
  proxy: false,
  timeout: 60000,
});
