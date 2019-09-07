import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payees.reducer';
import { IPayees } from 'app/shared/model/payees.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPayeesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PayeesDetail extends React.Component<IPayeesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { payeesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Payees [<b>{payeesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="payeeID">Payee ID</span>
            </dt>
            <dd>{payeesEntity.payeeID}</dd>
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{payeesEntity.email}</dd>
            <dt>
              <span id="firstName">First Name</span>
            </dt>
            <dd>{payeesEntity.firstName}</dd>
            <dt>
              <span id="lastName">Last Name</span>
            </dt>
            <dd>{payeesEntity.lastName}</dd>
            <dt>
              <span id="telephone">Telephone</span>
            </dt>
            <dd>{payeesEntity.telephone}</dd>
            <dt>User TO Payee</dt>
            <dd>{payeesEntity.userTOPayeeLogin ? payeesEntity.userTOPayeeLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/payees" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/payees/${payeesEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ payees }: IRootState) => ({
  payeesEntity: payees.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PayeesDetail);
