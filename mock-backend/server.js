const jsonServer = require('json-server');
const express = require('express'); // Require Express.js
const server = jsonServer.create();
const path = require('path');
const router = jsonServer.router(path.join(__dirname, 'db.json'));
const middlewares = jsonServer.defaults();
const bodyParser = require('body-parser');

// Use default middlewares (logger, static, cors, and no-cache)
server.use(middlewares);

// Serve images from the /images endpoint using Express static middleware
server.use('/images', express.static(path.join(__dirname, 'images')));

// Parse JSON bodies
server.use(bodyParser.json());

// Custom route for fetching product by barcode
server.get('/product', (req, res) => {
  const id = req.query.id;
  const product = router.db.get('products').find({ id }).value();
  if (product) {
    res.jsonp(product);
  } else {
    res.status(404).jsonp({ error: "Product not found" });
  }
});

// Custom route for fetching discount by code
server.get('/discount', (req, res) => {
  const code = req.query.code;
  const discount = router.db.get('discounts').find({ code }).value();
  if (discount) {
    res.jsonp(discount);
  } else {
    res.status(404).jsonp({ error: "Discount code not found" });
  }
});

// Custom route for admin login
server.post('/adminlogin', (req, res) => {
  const { username, password } = req.body;
  const user = router.db.get('adminUsers').find({ username, password }).value();
  if (user) {
    res.jsonp({ message: "Login successful" });
  } else {
    res.status(401).jsonp({ error: "Invalid username or password" });
  }
});

// Custom route for printing receipts
server.post('/print', (req, res) => {
  const { paymentMethod, cartObjects, discountCode, discountValue } = req.body;
  // Here you can add logic to handle the print request as needed
  // For mocking purposes, we'll assume it's always successful
  router.db.get('prints').push({
    id: Date.now(),
    paymentMethod,
    cartObjects,
    discountCode,
    discountValue,
    timestamp: new Date().toISOString()
  }).write();
  
  res.jsonp({ message: "Print successful" });
});

// Use default router
server.use(router);

const PORT = 3000;
server.listen(PORT, () => {
  console.log(`JSON Server is running on port ${PORT}`);
}); 