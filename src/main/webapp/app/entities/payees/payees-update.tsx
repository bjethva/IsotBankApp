import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './payees.reducer';
import { IPayees } from 'app/shared/model/payees.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPayeesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPayeesUpdateState {
  isNew: boolean;
  userTOPayeeId: string;
}

export class PayeesUpdate extends React.Component<IPayeesUpdateProps, IPayeesUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userTOPayeeId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { payeesEntity } = this.props;
      const entity = {
        ...payeesEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/payees');
  };

  render() {
    const { payeesEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="isotBankApp.payees.home.createOrEditLabel">Create or edit a Payees</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : payeesEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="payees-id">ID</Label>
                    <AvInput id="payees-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="payeeIDLabel" for="payees-payeeID">
                    Payee ID
                  </Label>
                  <AvField id="payees-payeeID" type="string" className="form-control" name="payeeID" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="payees-email">
                    Email
                  </Label>
                  <AvField
                    id="payees-email"
                    type="text"
                    name="email"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      pattern: { value: '^[_.@A-Za-z0-9-]*$', errorMessage: "This field should follow pattern for '^[_.@A-Za-z0-9-]*.." }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="firstNameLabel" for="payees-firstName">
                    First Name
                  </Label>
                  <AvField
                    id="payees-firstName"
                    type="text"
                    name="firstName"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="payees-lastName">
                    Last Name
                  </Label>
                  <AvField
                    id="payees-lastName"
                    type="text"
                    name="lastName"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="telephoneLabel" for="payees-telephone">
                    Telephone
                  </Label>
                  <AvField id="payees-telephone" type="text" name="telephone" />
                </AvGroup>
                <AvGroup>
                  <Label for="payees-userTOPayee">User TO Payee</Label>
                  <AvInput id="payees-userTOPayee" type="select" className="form-control" name="userTOPayeeId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/payees" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  payeesEntity: storeState.payees.entity,
  loading: storeState.payees.loading,
  updating: storeState.payees.updating,
  updateSuccess: storeState.payees.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PayeesUpdate);
