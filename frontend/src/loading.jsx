import Spinner from "./utils/spinner";

export default function Loading() {
  return <div className="columns is-centered">
    <div className="column is-narrow">
      <Spinner />
    </div>
  </div>
}