import { useRouteError } from "react-router-dom"

export default function ErrorPage() {
  const error = useRouteError()
  console.error(error)

  return <div className="columns">
    <div className="column is-full">
      <div className="columns is-centered">
        <div className="column is-narrow">
          <p>Ooops, error occurred!</p>
        </div>
      </div>
      <div className="columns is-centered">
        <div className="column is-narrow">
          <p>
            <i>{error.statusText || error.message}</i>
          </p>
        </div>
      </div>
    </div>
  </div>
}