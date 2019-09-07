import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './payees.reducer';
import { IPayees } from 'app/shared/model/payees.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPayeesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Payees extends React.Component<IPayeesProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { payeesList, match } = this.props;
    return (
      <div>
        <h2 id="payees-heading">
          Payees
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Payees
          </Link>
        </h2>
        <div className="table-responsive">
          {payeesList && payeesList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Payee ID</th>
                  <th>Email</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Telephone</th>
                  <th>User TO Payee</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {payeesList.map((payees, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${payees.id}`} color="link" size="sm">
                        {payees.id}
                      </Button>
                    </td>
                    <td>{payees.payeeID}</td>
                    <td>{payees.email}</td>
                    <td>{payees.firstName}</td>
                    <td>{payees.lastName}</td>
                    <td>{payees.telephone}</td>
                    <td>{payees.userTOPayeeLogin ? payees.userTOPayeeLogin : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${payees.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${payees.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${payees.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Payees found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ payees }: IRootState) => ({
  payeesList: payees.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Payees);
