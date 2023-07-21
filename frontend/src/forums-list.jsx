import { Link } from "react-router-dom"
import { useForums } from "./api/forum"
import CreateForumForm from "./forum/create-forum-form"
import Loading from "./loading"
import { useAuthStore } from "./store/authentication"

export default function Forums() {
  const { forums, isLoading } = useForums()
  const userRole = useAuthStore(store => store.role)

  const forumsRows = forums?.map(forum => <tr key={forum.id}>
    <td><Link to={'/' + forum.id}>{forum.name}</Link> /{forum.id}/</td>
    {/* <td>10</td>
    <td>10</td> */}
  </tr>)

  const forumsTable = <table className="table is-fullwidth">
    <thead>
      <tr>
        <th>Форум</th>
        {/* <th>Тредов</th>
        <th>Постов</th> */}
      </tr>
    </thead>
    <tbody>
      {forumsRows}
    </tbody>
  </table>

  const createForumForm = <div className="columns is-centered">
    <div className="column is-narrow box">
      <CreateForumForm />
    </div> 
  </div>

  return <>
    <div className="columns is-centered">
      <div className="column is-four-fifths">
        <div className="columns is-centered" style={{marginBottom: '2em'}}>
          <div className="column ">
            {isLoading && <Loading />}
            {forumsRows && forumsTable}
          </div>
        </div>
        {(userRole === 'ADMIN' || userRole === 'OWNER') && createForumForm}
      </div>
    </div>
  </>
}