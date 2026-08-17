/*
 * Lógica principal del frontend de Mapuescuela.
 * Se organizaron las funciones por vista para facilitar las pruebas,
 * el mantenimiento y la futura integración con el backend.
 */

const STORAGE = {
  products: "mapuescuela_products_v3",
  cart: "mapuescuela_cart",
  orders: "mapuescuela_orders"
};

const DEFAULT_PRODUCTS = [
  {
    id: 1,
    nombre: "Colección de libros juveniles",
    descripcion: "Set de 5 libros usados en buen estado. Ideal para lectura escolar y recreativa.",
    categoria: "Libros",
    precio: 12000,
    stock: 2,
    estado: "DISPONIBLE",
    imagen: "img-books-used.jpg"
  },
  {
    id: 2,
    nombre: "Escritorio de madera",
    descripcion: "Escritorio compacto usado, firme y funcional para estudio o teletrabajo.",
    categoria: "Muebles",
    precio: 28000,
    stock: 1,
    estado: "DISPONIBLE",
    imagen: "https://images.unsplash.com/photo-1572521165329-b197f9ea3da6?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1200"
  },
  {
    id: 3,
    nombre: "Juego didáctico",
    descripcion: "Juego educativo para niñas y niños. Completo y en buenas condiciones.",
    categoria: "Juguetes",
    precio: 7000,
    stock: 3,
    estado: "DISPONIBLE",
    imagen: "https://images.unsplash.com/photo-1545558014-8692077e9b5c?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1200"
  },
  {
    id: 4,
    nombre: "Silla de escritorio",
    descripcion: "Silla usada con respaldo firme. Presenta señales normales de uso.",
    categoria: "Muebles",
    precio: 18000,
    stock: 1,
    estado: "DISPONIBLE",
    imagen: "img-chair.webp"
  },
  {
    id: 5,
    nombre: "Mochila escolar",
    descripcion: "Mochila de tamaño mediano, limpia y lista para utilizar.",
    categoria: "Accesorios",
    precio: 6000,
    stock: 2,
    estado: "DISPONIBLE",
    imagen: "https://images.unsplash.com/photo-1546938576-6e6a64f317cc?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1200"
  },
  {
    id: 6,
    nombre: "Set de cuentos infantiles",
    descripcion: "Colección de cuentos usados, todos en buen estado para lectura infantil.",
    categoria: "Libros",
    precio: 9000,
    stock: 1,
    estado: "DISPONIBLE",
    imagen: "img-stories.webp"
  }
];

function getJSON(key, fallback) {
  try { return JSON.parse(localStorage.getItem(key)) ?? fallback; }
  catch { return fallback; }
}
function setJSON(key, value) { localStorage.setItem(key, JSON.stringify(value)); }

// Inicializa datos de demostración para que el frontend pueda probarse
// aunque el backend todavía no esté conectado.
function initializeData() {
  if (!localStorage.getItem(STORAGE.products)) setJSON(STORAGE.products, DEFAULT_PRODUCTS);
  if (!localStorage.getItem(STORAGE.cart)) setJSON(STORAGE.cart, []);
  if (!localStorage.getItem(STORAGE.orders)) setJSON(STORAGE.orders, []);
}

function products() { return getJSON(STORAGE.products, []); }
function cart() { return getJSON(STORAGE.cart, []); }
function orders() { return getJSON(STORAGE.orders, []); }

function money(value) {
  return new Intl.NumberFormat("es-CL", { style: "currency", currency: "CLP", maximumFractionDigits: 0 }).format(value);
}

function productById(id) { return products().find(p => p.id === Number(id)); }

// Actualiza el contador visible del carrito cada vez que cambia su contenido.
function updateCartCount() {
  const count = cart().reduce((acc, item) => acc + item.cantidad, 0);
  document.querySelectorAll("#cart-count").forEach(el => el.textContent = count);
}

function toast(message) {
  let el = document.getElementById("toast");
  if (!el) {
    el = document.createElement("div");
    el.id = "toast";
    el.className = "toast";
    document.body.appendChild(el);
  }
  el.textContent = message;
  el.classList.add("show");
  setTimeout(() => el.classList.remove("show"), 2200);
}

// Agrega productos al carrito y evita superar el stock disponible.
function addToCart(id) {
  const p = productById(id);
  if (!p || p.stock <= 0 || p.estado !== "DISPONIBLE") return;
  const c = cart();
  const current = c.find(x => x.productId === p.id);
  if (current) {
    if (current.cantidad >= p.stock) return toast("No hay más unidades disponibles.");
    current.cantidad += 1;
  } else {
    c.push({ productId: p.id, cantidad: 1 });
  }
  setJSON(STORAGE.cart, c);
  updateCartCount();
  toast("Producto agregado al carrito.");
}

// Carga el catálogo y aplica búsqueda y filtro por categoría.
function renderCatalog() {
  const grid = document.getElementById("product-grid");
  if (!grid) return;

  const category = document.getElementById("category-filter");
  const search = document.getElementById("search-input");
  const all = products().filter(p => p.estado === "DISPONIBLE");

  [...new Set(all.map(p => p.categoria))].sort().forEach(cat => {
    const opt = document.createElement("option");
    opt.value = cat; opt.textContent = cat;
    category.appendChild(opt);
  });

  const draw = () => {
    const term = search.value.trim().toLowerCase();
    const selected = category.value;
    const filtered = all.filter(p =>
      (!selected || p.categoria === selected) &&
      (!term || `${p.nombre} ${p.descripcion}`.toLowerCase().includes(term))
    );

    grid.innerHTML = filtered.map(p => `
      <article class="product-card">
        <button class="product-visual" data-detail="${p.id}" aria-label="Ver ${p.nombre}">
          <img src="${p.imagen}" alt="${p.nombre}" loading="lazy">
        </button>
        <div class="product-body">
          <span class="badge">${p.categoria}</span>
          <h3>${p.nombre}</h3>
          <p>${p.descripcion}</p>
          <div class="product-meta"><span>Stock: ${p.stock}</span><strong>${money(p.precio)}</strong></div>
          <div class="card-actions">
            <button class="btn ghost" data-detail="${p.id}">Ver detalle</button>
            <button class="btn primary" data-add="${p.id}">Agregar</button>
          </div>
        </div>
      </article>`).join("");

    document.getElementById("empty-products").classList.toggle("hidden", filtered.length > 0);
    grid.querySelectorAll("[data-add]").forEach(btn => btn.addEventListener("click", () => addToCart(btn.dataset.add)));
    grid.querySelectorAll("[data-detail]").forEach(btn => btn.addEventListener("click", () => openProductModal(btn.dataset.detail)));
  };

  search.addEventListener("input", draw);
  category.addEventListener("change", draw);
  draw();
}

// Muestra el detalle de un producto sin salir del catálogo.
function openProductModal(id) {
  const p = productById(id);
  const modal = document.getElementById("product-modal");
  if (!p || !modal) return;
  document.getElementById("modal-content").innerHTML = `
    <div class="detail-visual"><img src="${p.imagen}" alt="${p.nombre}"></div>
    <span class="badge">${p.categoria}</span>
    <h2>${p.nombre}</h2>
    <p>${p.descripcion}</p>
    <div class="product-meta large"><span>Stock disponible: ${p.stock}</span><strong>${money(p.precio)}</strong></div>
    <button id="modal-add" class="btn primary block">Agregar al carrito</button>`;
  modal.classList.remove("hidden");
  modal.setAttribute("aria-hidden", "false");
  document.getElementById("modal-add").onclick = () => { addToCart(p.id); closeProductModal(); };
}
function closeProductModal() {
  const modal = document.getElementById("product-modal");
  if (modal) { modal.classList.add("hidden"); modal.setAttribute("aria-hidden", "true"); }
}

// Construye la vista del carrito y calcula cantidades y total de compra.
function renderCart() {
  const wrap = document.getElementById("cart-items");
  if (!wrap) return;
  const c = cart();
  const empty = document.getElementById("empty-cart");
  const summary = document.getElementById("cart-summary");

  if (!c.length) {
    wrap.innerHTML = "";
    empty.classList.remove("hidden");
    summary.classList.add("hidden");
    return;
  }

  empty.classList.add("hidden");
  summary.classList.remove("hidden");

  wrap.innerHTML = c.map(item => {
    const p = productById(item.productId);
    if (!p) return "";
    return `
      <article class="cart-item">
        <div class="cart-icon"><img src="${p.imagen}" alt="${p.nombre}"></div>
        <div class="cart-info">
          <span class="badge">${p.categoria}</span>
          <h3>${p.nombre}</h3>
          <small>${money(p.precio)} c/u</small>
        </div>
        <div class="qty">
          <button data-minus="${p.id}">−</button>
          <strong>${item.cantidad}</strong>
          <button data-plus="${p.id}">+</button>
        </div>
        <strong>${money(p.precio * item.cantidad)}</strong>
        <button class="icon-btn danger" data-remove="${p.id}" title="Eliminar">×</button>
      </article>`;
  }).join("");

  const itemCount = c.reduce((a, b) => a + b.cantidad, 0);
  const total = c.reduce((sum, item) => {
    const p = productById(item.productId);
    return sum + (p ? p.precio * item.cantidad : 0);
  }, 0);

  document.getElementById("summary-items").textContent = itemCount;
  document.getElementById("summary-total").textContent = money(total);

  wrap.querySelectorAll("[data-minus]").forEach(b => b.onclick = () => changeQty(Number(b.dataset.minus), -1));
  wrap.querySelectorAll("[data-plus]").forEach(b => b.onclick = () => changeQty(Number(b.dataset.plus), 1));
  wrap.querySelectorAll("[data-remove]").forEach(b => b.onclick = () => removeCartItem(Number(b.dataset.remove)));

  document.getElementById("clear-cart").onclick = () => {
    setJSON(STORAGE.cart, []);
    renderCart();
    updateCartCount();
  };
}

// Permite aumentar o disminuir unidades respetando el stock disponible.
function changeQty(id, delta) {
  const c = cart();
  const item = c.find(x => x.productId === id);
  const p = productById(id);
  if (!item || !p) return;
  item.cantidad = Math.max(1, Math.min(p.stock, item.cantidad + delta));
  setJSON(STORAGE.cart, c);
  renderCart();
  updateCartCount();
}
function removeCartItem(id) {
  setJSON(STORAGE.cart, cart().filter(x => x.productId !== id));
  renderCart(); updateCartCount();
}

// Prepara el formulario de compra y muestra el resumen antes de generar el pedido.
function renderCheckout() {
  const list = document.getElementById("checkout-items");
  if (!list) return;
  const c = cart();
  if (!c.length) {
    location.href = "carrito.html";
    return;
  }
  list.innerHTML = c.map(item => {
    const p = productById(item.productId);
    return `<div><span>${p.nombre} × ${item.cantidad}</span><strong>${money(p.precio * item.cantidad)}</strong></div>`;
  }).join("");
  const total = c.reduce((sum, item) => {
    const p = productById(item.productId);
    return sum + p.precio * item.cantidad;
  }, 0);
  document.getElementById("checkout-total").textContent = money(total);

  const form = document.getElementById("checkout-form");
  form.querySelectorAll('input[name="entrega"]').forEach(r => {
    r.addEventListener("change", () => {
      const despacho = form.querySelector('input[name="entrega"]:checked').value === "DESPACHO";
      document.getElementById("shipping-fields").classList.toggle("hidden", !despacho);
      form.elements.direccion.required = despacho;
    });
  });

  form.addEventListener("submit", (e) => {
    e.preventDefault();
    createOrder(new FormData(form));
  });
}

// Genera un pedido de demostración con un código único para poder consultarlo después.
function createOrder(formData) {
  const c = cart();
  const ps = products();
  const invalid = c.some(item => {
    const p = ps.find(x => x.id === item.productId);
    return !p || p.stock < item.cantidad || p.estado !== "DISPONIBLE";
  });
  if (invalid) return toast("El stock cambió. Revisa tu carrito.");

  const total = c.reduce((sum, item) => {
    const p = ps.find(x => x.id === item.productId);
    return sum + p.precio * item.cantidad;
  }, 0);

  const code = `MAP-${Date.now().toString().slice(-6)}`;
  const hasReceipt = formData.get("comprobante")?.name;
  const estado = hasReceipt ? "PAGO_EN_REVISION" : "PENDIENTE_DE_PAGO";

  const order = {
    id: Date.now(),
    codigo: code,
    creado: new Date().toISOString(),
    limitePago: new Date(Date.now() + 24*60*60*1000).toISOString(),
    cliente: {
      nombre: formData.get("nombre"),
      email: formData.get("email"),
      telefono: formData.get("telefono"),
      comuna: formData.get("comuna")
    },
    entrega: formData.get("entrega"),
    direccion: formData.get("direccion") || "",
    comprobante: hasReceipt || "",
    estado,
    items: c.map(x => ({...x})),
    total
  };

  const os = orders();
  os.push(order);
  setJSON(STORAGE.orders, os);
  setJSON(STORAGE.cart, []);
  sessionStorage.setItem("mapuescuela_last_order", code);

  document.getElementById("checkout-form").classList.add("hidden");
  const msg = document.getElementById("checkout-message");
  msg.classList.remove("hidden");
  msg.classList.add("success-box");
  msg.innerHTML = `
    <strong>¡Pedido generado correctamente!</strong>
    <span>Tu código es <b>${code}</b>.</span>
    <span>Estado inicial: ${statusLabel(estado)}.</span>
    <a class="btn primary" href="estado.html?codigo=${encodeURIComponent(code)}">Ver estado del pedido</a>`;
  updateCartCount();
}

function statusLabel(status) {
  const map = {
    PENDIENTE_DE_PAGO: "Pendiente de pago",
    PAGO_EN_REVISION: "Pago en revisión",
    PAGO_RECHAZADO: "Pago rechazado",
    PAGO_APROBADO: "Pago aprobado",
    EN_PREPARACION: "En preparación",
    LISTO_PARA_RETIRO: "Listo para retiro",
    ENVIADO: "Enviado",
    FINALIZADO: "Finalizado",
    CANCELADO: "Cancelado"
  };
  return map[status] || status;
}

function statusClass(status) {
  if (["PAGO_RECHAZADO","CANCELADO"].includes(status)) return "danger";
  if (["FINALIZADO","PAGO_APROBADO","LISTO_PARA_RETIRO"].includes(status)) return "success";
  if (["PAGO_EN_REVISION","EN_PREPARACION","ENVIADO"].includes(status)) return "warning";
  return "neutral";
}

// Permite consultar un pedido utilizando el código generado durante la compra.
function renderTracking() {
  const form = document.getElementById("track-form");
  if (!form) return;
  const params = new URLSearchParams(location.search);
  const input = document.getElementById("track-code");
  const initial = params.get("codigo") || sessionStorage.getItem("mapuescuela_last_order") || "";
  input.value = initial;

  const run = () => {
    const code = input.value.trim().toUpperCase();
    const order = orders().find(o => o.codigo.toUpperCase() === code);
    const result = document.getElementById("track-result");
    result.classList.remove("hidden");
    if (!order) {
      result.innerHTML = `<div class="empty"><h3>Pedido no encontrado</h3><p>Revisa el código e inténtalo nuevamente.</p></div>`;
      return;
    }
    result.innerHTML = orderDetailHTML(order);
  };

  form.addEventListener("submit", e => { e.preventDefault(); run(); });
  if (initial) run();
}

function orderDetailHTML(o) {
  return `
    <div class="order-top">
      <div><span class="eyebrow">Pedido ${o.codigo}</span><h2>${o.cliente.nombre}</h2></div>
      <span class="status ${statusClass(o.estado)}">${statusLabel(o.estado)}</span>
    </div>
    <div class="order-grid">
      <div><small>Total</small><strong>${money(o.total)}</strong></div>
      <div><small>Modalidad</small><strong>${o.entrega === "RETIRO" ? "Retiro" : "Despacho"}</strong></div>
      <div><small>Fecha</small><strong>${new Date(o.creado).toLocaleString("es-CL")}</strong></div>
      <div><small>Límite comprobante</small><strong>${new Date(o.limitePago).toLocaleString("es-CL")}</strong></div>
    </div>
    <h3>Productos</h3>
    <div class="mini-list">${o.items.map(i => {
      const p = productById(i.productId);
      return `<div><span>${p?.nombre || "Producto"} × ${i.cantidad}</span><strong>${money((p?.precio || 0)*i.cantidad)}</strong></div>`;
    }).join("")}</div>
    <div class="process-strip">
      ${["PENDIENTE_DE_PAGO","PAGO_EN_REVISION","PAGO_APROBADO","EN_PREPARACION", o.entrega === "RETIRO" ? "LISTO_PARA_RETIRO" : "ENVIADO","FINALIZADO"]
        .map(s => `<span class="${o.estado === s ? "current" : ""}">${statusLabel(s)}</span>`).join("")}
    </div>`;
}

// Implementa el panel de administración para revisar pedidos y gestionar productos.
function renderAdmin() {
  const ordersTable = document.getElementById("orders-table");
  if (!ordersTable) return;

  const refresh = () => {
    const ps = products();
    const os = orders();
    document.getElementById("stat-products").textContent = ps.length;
    document.getElementById("stat-orders").textContent = os.length;
    document.getElementById("stat-review").textContent = os.filter(o => o.estado === "PAGO_EN_REVISION").length;
    document.getElementById("stat-approved").textContent = os.filter(o => ["PAGO_APROBADO","EN_PREPARACION","LISTO_PARA_RETIRO","ENVIADO","FINALIZADO"].includes(o.estado)).length;

    ordersTable.innerHTML = os.slice().reverse().map(o => `
      <tr>
        <td><strong>${o.codigo}</strong><br><small>${new Date(o.creado).toLocaleDateString("es-CL")}</small></td>
        <td>${o.cliente.nombre}<br><small>${o.cliente.email}</small></td>
        <td>${money(o.total)}</td>
        <td>${o.entrega === "RETIRO" ? "Retiro" : "Despacho"}</td>
        <td><span class="status ${statusClass(o.estado)}">${statusLabel(o.estado)}</span></td>
        <td class="actions-cell">${adminActions(o)}</td>
      </tr>`).join("");
    document.getElementById("no-orders").classList.toggle("hidden", os.length > 0);

    document.getElementById("products-table").innerHTML = ps.map(p => `
      <tr>
        <td><div class="admin-product"><img src="${p.imagen}" alt="${p.nombre}"><strong>${p.nombre}</strong></div></td>
        <td>${p.categoria}</td>
        <td>${money(p.precio)}</td>
        <td>${p.stock}</td>
        <td><span class="status ${p.estado === "DISPONIBLE" ? "success" : "neutral"}">${p.estado === "DISPONIBLE" ? "Disponible" : "No disponible"}</span></td>
        <td><button class="btn tiny ghost" data-edit-product="${p.id}">Editar</button></td>
      </tr>`).join("");

    document.querySelectorAll("[data-order-action]").forEach(btn => {
      btn.onclick = () => updateOrderStatus(Number(btn.dataset.orderId), btn.dataset.orderAction);
    });
    document.querySelectorAll("[data-edit-product]").forEach(btn => {
      btn.onclick = () => openProductForm(Number(btn.dataset.editProduct));
    });
  };

  document.querySelectorAll(".tab").forEach(tab => {
    tab.onclick = () => {
      document.querySelectorAll(".tab").forEach(t => t.classList.remove("active"));
      tab.classList.add("active");
      document.querySelectorAll(".admin-panel").forEach(p => p.classList.add("hidden"));
      document.getElementById(`tab-${tab.dataset.tab}`).classList.remove("hidden");
    };
  });

  document.getElementById("new-product").onclick = () => openProductForm();
  document.getElementById("product-form-close").onclick = closeProductForm;
  document.getElementById("product-form").onsubmit = saveProduct;
  document.getElementById("reset-demo").onclick = () => {
    if (confirm("¿Restablecer productos, pedidos y carrito de demostración?")) {
      setJSON(STORAGE.products, DEFAULT_PRODUCTS);
      setJSON(STORAGE.orders, []);
      setJSON(STORAGE.cart, []);
      refresh(); updateCartCount(); toast("Demo restablecida.");
    }
  };

  window.adminRefresh = refresh;
  refresh();
}

function adminActions(o) {
  if (o.estado === "PENDIENTE_DE_PAGO") return `<button class="btn tiny ghost" data-order-action="PAGO_EN_REVISION" data-order-id="${o.id}">Marcar comprobante</button>`;
  if (o.estado === "PAGO_EN_REVISION") return `
    <button class="btn tiny primary" data-order-action="PAGO_APROBADO" data-order-id="${o.id}">Aprobar</button>
    <button class="btn tiny danger-btn" data-order-action="PAGO_RECHAZADO" data-order-id="${o.id}">Rechazar</button>`;
  if (o.estado === "PAGO_APROBADO") return `<button class="btn tiny primary" data-order-action="EN_PREPARACION" data-order-id="${o.id}">Preparar</button>`;
  if (o.estado === "EN_PREPARACION") {
    const next = o.entrega === "RETIRO" ? "LISTO_PARA_RETIRO" : "ENVIADO";
    return `<button class="btn tiny primary" data-order-action="${next}" data-order-id="${o.id}">${o.entrega === "RETIRO" ? "Listo retiro" : "Registrar envío"}</button>`;
  }
  if (["LISTO_PARA_RETIRO","ENVIADO"].includes(o.estado)) return `<button class="btn tiny primary" data-order-action="FINALIZADO" data-order-id="${o.id}">Finalizar</button>`;
  return `<small>Sin acciones</small>`;
}

// Actualiza el estado del pedido según el avance del proceso de venta.
// Cuando se aprueba un pago también se descuenta el stock en esta versión local.
function updateOrderStatus(id, status) {
  const os = orders();
  const order = os.find(o => o.id === id);
  if (!order) return;

  if (status === "PAGO_APROBADO" && order.estado !== "PAGO_APROBADO") {
    const ps = products();
    for (const item of order.items) {
      const p = ps.find(x => x.id === item.productId);
      if (!p || p.stock < item.cantidad) return toast("Stock insuficiente para aprobar el pedido.");
    }
    order.items.forEach(item => {
      const p = ps.find(x => x.id === item.productId);
      p.stock -= item.cantidad;
      if (p.stock === 0) p.estado = "NO_DISPONIBLE";
    });
    setJSON(STORAGE.products, ps);
  }
  order.estado = status;
  setJSON(STORAGE.orders, os);
  window.adminRefresh?.();
  toast(`Pedido actualizado: ${statusLabel(status)}.`);
}

function openProductForm(id = null) {
  const modal = document.getElementById("product-form-modal");
  const form = document.getElementById("product-form");
  form.reset();
  form.elements.id.value = "";
  document.getElementById("product-form-title").textContent = id ? "Editar producto" : "Agregar producto";
  if (id) {
    const p = productById(id);
    form.elements.id.value = p.id;
    form.elements.nombre.value = p.nombre;
    form.elements.categoria.value = p.categoria;
    form.elements.precio.value = p.precio;
    form.elements.stock.value = p.stock;
    form.elements.descripcion.value = p.descripcion;
    form.elements.estado.value = p.estado;
  }
  modal.classList.remove("hidden");
}
function closeProductForm() { document.getElementById("product-form-modal")?.classList.add("hidden"); }
// Guarda productos nuevos o modificaciones realizadas desde Administración.
function saveProduct(e) {
  e.preventDefault();
  const f = new FormData(e.target);
  const ps = products();
  const id = Number(f.get("id"));
  const obj = {
    id: id || Date.now(),
    nombre: f.get("nombre"),
    categoria: f.get("categoria"),
    precio: Number(f.get("precio")),
    stock: Number(f.get("stock")),
    descripcion: f.get("descripcion"),
    estado: f.get("estado"),
    imagen: id ? (ps.find(p => p.id === id)?.imagen || DEFAULT_PRODUCTS[0].imagen) : DEFAULT_PRODUCTS[0].imagen
  };
  if (id) {
    const idx = ps.findIndex(p => p.id === id);
    ps[idx] = obj;
  } else ps.push(obj);
  setJSON(STORAGE.products, ps);
  closeProductForm();
  window.adminRefresh?.();
  toast("Producto guardado.");
}

// Detecta la página abierta y ejecuta solo la lógica necesaria para esa vista.
document.addEventListener("DOMContentLoaded", () => {
  initializeData();
  updateCartCount();

  const page = document.body.dataset.page;
  if (page === "catalogo") renderCatalog();
  if (page === "carrito") renderCart();
  if (page === "checkout") renderCheckout();
  if (page === "estado") renderTracking();
  if (page === "admin") renderAdmin();

  document.getElementById("modal-close")?.addEventListener("click", closeProductModal);
  document.getElementById("product-modal")?.addEventListener("click", e => {
    if (e.target.id === "product-modal") closeProductModal();
  });
});
