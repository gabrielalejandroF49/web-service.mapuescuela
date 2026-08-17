/**
 * Archivo reservado para centralizar la futura comunicación SOAP.
 * Se mantiene separado de app.js para no mezclar la interfaz con la integración.
 * Mientras el backend no esté listo, el frontend continúa funcionando en modo demo.
 */

const SOAP_CONFIG = {
  enabled: false,
  endpoint: "http://localhost:8080/ws",
  namespace: "http://mapuescuela.cl/ws"
};

// Construye y envía una solicitud SOAP en formato XML al endpoint configurado.
async function soapRequest(operation, innerXml = "") {
  if (!SOAP_CONFIG.enabled) {
    throw new Error("SOAP desactivado: el frontend está funcionando en modo demo.");
  }

  // El mensaje SOAP se estructura dentro de un Envelope XML.
  const envelope = `<?xml version="1.0" encoding="UTF-8"?>
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:map="${SOAP_CONFIG.namespace}">
    <soapenv:Header/>
    <soapenv:Body>
      <map:${operation}>
        ${innerXml}
      </map:${operation}>
    </soapenv:Body>
  </soapenv:Envelope>`;

  // fetch envía la petición HTTP desde el navegador al servicio.
  const response = await fetch(SOAP_CONFIG.endpoint, {
    method: "POST",
    headers: {
      "Content-Type": "text/xml; charset=utf-8",
      "SOAPAction": operation
    },
    body: envelope
  });

  if (!response.ok) {
    throw new Error(`Error SOAP ${response.status}: ${response.statusText}`);
  }

  // La respuesta se recibe como XML para luego interpretarla desde JavaScript.
  const xmlText = await response.text();
  return new DOMParser().parseFromString(xmlText, "text/xml");
}
