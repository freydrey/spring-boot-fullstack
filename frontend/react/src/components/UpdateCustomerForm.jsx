import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import {FormLabel, Alert, AlertIcon, Input, Box, Select, Button, Stack} from "@chakra-ui/react";
//import { updateCustomer } from "../services/client.jsx";
import { updateCustomer } from "../services/client.jsx";
import {successNotification, errorNotification} from "../services/notification.js";

const MyTextInput = ({ label, ...props }) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Input className="text-input" {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
        <AlertIcon/>
        {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};

/*const MyCheckbox = ({ children, ...props }) => {
  // React treats radios and checkbox inputs differently from other input types: select and textarea.
  // Formik does this too! When you specify `type` to useField(), it will
  // return the correct bag of props for you -- a `checked` prop will be included
  // in `field` alongside `name`, `value`, `onChange`, and `onBlur`
  const [field, meta] = useField({ ...props, type: 'checkbox' });
  return (
    <div>
      <label className="checkbox-input">
        <input type="checkbox" {...field} {...props} />
        {children}
      </label>
      {meta.touched && meta.error ? (
        <div className="error">{meta.error}</div>
      ) : null}
    </div>
  );
};*/


// And now we can use these
const UpdateCustomerForm = ({fetchCustomers, initialValues, customerId}) => {
  return (
    <>
      <Formik
        initialValues={initialValues}
        validationSchema={Yup.object({
          name: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .required('Required'),
          email: Yup.string()
            .email('Invalid email address')
            .required('Required'),
          age: Yup.number()
            .min(16, 'Must be atleast 16 years')
            .max(100, 'Must be less than 100 years')
            .required(),

        })}
        onSubmit={(updatedCustomer, { setSubmitting }) => {
        setSubmitting(true);
          updateCustomer(customerId, updatedCustomer)
              .then(res => {
                  console.log(res);
                  successNotification(
                      "Customer Updated",
                      `${updatedCustomer.name} was successfully updated`
                  )
                  fetchCustomers();
              }).catch(err => {
                  console.log(err);
                  errorNotification(
                        err.code,
                        err.response.data.message
                  )
          }).finally( () => {
              setSubmitting(false);
          })
        }}
      >
      {({isValid, isSubmitting, dirty}) => (
        <Form>
                 <Stack spacing={"24px"}>
                   <MyTextInput
                       label="Name"
                       name="name"
                       type="text"
                       placeholder="Jane"
                     />

                     <MyTextInput
                       label="Email Address"
                       name="email"
                       type="email"
                       placeholder="jane@formik.com"
                     />

                       <MyTextInput
                         label="Age"
                         name="age"
                         type="number"
                         placeholder="20"
                       />

                     <Button disabled={!(isValid && dirty) || isSubmitting} type="submit">Submit</Button>
                   </Stack>
                 </Form>
       )}
      </Formik>
    </>
  );
};

export default UpdateCustomerForm;