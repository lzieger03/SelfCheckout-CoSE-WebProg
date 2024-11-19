const jsonServer = require('json-server');
const express = require('express');
const server = jsonServer.create();
const path = require('path');
const router = jsonServer.router(path.join(__dirname, 'db.json'));
const middlewares = jsonServer.defaults();
const bodyParser = require('body-parser');

server.use(middlewares);

// Serve images from the /images endpoint using Express static middleware
server.use('/images', express.static(path.join(__dirname, 'images')));

server.use(bodyParser.json());


server.get('/product', (req, res) => {
  const id = req.query.id;
  const product = router.db.get('products').find({ id }).value();
  if (product) {
    res.jsonp(product);
  } else {
    res.status(404).jsonp({ error: "Product not found" });
  }
});


server.get('/discount', (req, res) => {
  const code = req.query.code;
  const discount = router.db.get('discounts').find({ code }).value();
  if (discount) {
    res.jsonp(discount);
  } else {
    res.status(404).jsonp({ error: "Discount code not found" });
  }
});


server.post('/adminlogin', (req, res) => {
  const { username, password } = req.body;
  const user = router.db.get('adminUsers').find({ username, password }).value();
  if (user) {
    res.jsonp({ message: "Login successful" });
  } else {
    res.status(401).jsonp({ error: "Invalid username or password" });
  }
});


server.post('/print', (req, res) => {
//  const { paymentMethod, cartObjects, discountCode, discountValue } = req.body;
//   router.db.get('prints').push({
//     id: Date.now(),
//     paymentMethod,
//     cartObjects,
//     discountCode,
//     discountValue,
//     timestamp: new Date().toISOString()
//   }).write();
  
  res.jsonp({ message: "Print successful" });
});

// Use default router
server.use(router);

const PORT = 3000;
server.listen(PORT, () => {
  console.log(`JSON Server is running on port ${PORT}`);
}); 