describe('End-to-End Add Pet Flow', () => {
  const baseURLforBE = "http://localhost:8090";
  const baseURLforFE = "http://localhost:5173";

  beforeEach(() => {
    cy.visit(baseURLforFE + '/login');
    cy.get('#email').type('antsimdim04@gmail.com');
    cy.get('#password').type('1234');
    cy.get('form').submit();
    cy.url().should('eq', baseURLforFE + '/account');
    cy.contains('Profile Information');

    // Navigate to Add Pet page
    cy.contains('Add Pet').click();
    cy.url().should('eq', baseURLforFE + '/addPet');

    cy.intercept('POST', baseURLforBE + `/pets`, (req) => {
      expect(req.body).to.deep.include({
        name: 'Buddy',
        breedId: 5, // Assuming 'Bulldog' has breedId = 5 in your backend
        gender: 'MALE',
        userId: 4, // Assuming userId is determined from session/backend logic
        weight: 20.2,
        vaccinationRecordsIds: [],
      });
      expect(req.body.birthdate).to.match(/2022-01-01/); // Confirming correct date format
      expect(req.body.image).to.be.a('string'); // Base64-encoded image
      req.reply({
        statusCode: 201,
        body: { message: 'Pet has been successfully created!' },
      });
    }).as('mockAddPet');
  });

  it('should log in, navigate to account, and add a pet successfully', () => {
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

    // Submit form and verify success message
    cy.get('form').submit();
    cy.contains('Pet has been successfully created!');
    cy.wait('@mockAddPet');
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
