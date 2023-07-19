import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import SignIn from "./authentication/sign-in";
import SignUp from "./authentication/sign-up";
import ErrorPage from "./error-page";
import Forums from "./forums-list";
import Posts from "./posts-list";
import Threads from "./thread/threads-list";
import AccountDashboard from "./personal-area/account-dashboard";

export default createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
    children: [
      // {
      //     path: "/",
      // },
      {
        path: ":forumId/",
        element: <Threads />,
      },
      {
        path: ":forumId/:threadId",
        element: <Posts />,
      },
      {
        path: "forums",
        element: <Forums />,
      },
      {
        path: "signIn",
        element: <SignIn />,
      },
      {
        path: "signUp",
        element: <SignUp />,
      },
      {
        path: "account",
        element: <AccountDashboard />,
      },
    ],
  },
])