describe('End-to-End Add Pet Flow', () => {
  const baseURLforBE = "http://localhost:8090";
  const baseURLforFE = "http://localhost:5173";

  let createdPetId;

  beforeEach(() => {
    // Navigate to Add Pet page
    cy.visit(baseURLforFE + '/login');
    cy.get('#email').type('antsimdim04@gmail.com');
    cy.get('#password').type('1234');
    cy.get('form').submit();
    cy.url().should('eq', baseURLforFE + '/account');
    cy.contains('Profile Information');
    cy.contains('Add Pet').click();
    cy.url().should('eq', baseURLforFE + '/addPet');
  });

  it('should log in, navigate to account, add a pet, verify its existence, and delete it successfully', () => {
    // Upload pet image
    cy.fixture('dog.jpg', 'base64').then((image) => {
      const blob = Cypress.Blob.base64StringToBlob(image, 'image/jpeg');
      const file = new File([blob], 'dog.jpg', { type: 'image/jpeg' });
      const dataTransfer = new DataTransfer();
      dataTransfer.items.add(file);

      cy.get('input[type="file"]').then((input) => {
        input[0].files = dataTransfer.files;
        input[0].dispatchEvent(new Event('change', { bubbles: true }));
      });
    });

    // Fill pet details
    cy.get('#name').type('Buddy');
    cy.selectReactSelectOption('#breed input', 'Bulldog');
    cy.get('#birthdate').type('2022-01-01', { force: true });
    cy.get('#weight').type('20.2');
    cy.get('#gender').select('male');

    // Submit the form
    cy.intercept('POST', `${baseURLforBE}/pets`).as('createPet');
    cy.get('form').submit();

    // Wait for pet creation and capture ID
    cy.wait('@createPet').then((interception) => {
      const responseBody = interception.response.body;
      createdPetId = responseBody.id;
      expect(createdPetId).to.exist; // Validate ID
    }).then(() => {
      // Verify the pet is displayed
      cy.visit(`${baseURLforFE}/account`);
      cy.contains('My Pets');
      cy.contains('Buddy');
      cy.contains('Bulldog');
      cy.contains('20.2 kg');
    }).then(() => {
      // Delete the pet
      cy.request({
        method: 'DELETE',
        url: `${baseURLforBE}/pets/${createdPetId}`,
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        },
      }).then((response) => {
        expect(response.status).to.eq(204);
      });
    }).then(() => {
      // Verify the pet no longer exists
      cy.request({
        method: 'GET',
        url: `${baseURLforBE}/pets/${createdPetId}`,
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // Pass token
        },
        failOnStatusCode: false, // Allow non-2xx responses
      }).then((response) => {
        expect(response.status).to.eq(404); // Verify pet is deleted
      });
    });
  });

  it('should display validation errors for missing or incorrect input', () => {
    cy.visit(baseURLforFE + '/addPet');

    // Try submitting without filling fields
    cy.get('form').submit();

    // Validation messages
    cy.contains('Name is required.');
    cy.contains('Breed is required.');
    cy.contains('Birthdate is required.');
    cy.contains('Weight must be greater than 0.');
    cy.contains('Gender is required.');
  });

  it('should prevent invalid vaccination selection based on age', () => {
    // Fill form with invalid birthdate
    cy.get('#name').type('Buddy');
    cy.selectReactSelectOption('#breed input', 'Bulldog');
    cy.get('#birthdate').type('2024-12-01', { force: true }); // Future date
    cy.get('#weight').type('15');
    cy.get('#gender').select('male');

    // Attempt to select an invalid vaccination
    cy.get('[data-cy="vaccination-checkbox-13"]').check(); // Updated selector

    // Submit form and verify error
    cy.get('form').submit();
    cy.contains(
      'Vaccination "Rabies" is not allowed for dogs younger than 16 weeks.'
    );
  });
});
