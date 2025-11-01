# ☁️ n8n Workflows

Workflows n8n para processar comandos de voz e gerar JSON estruturado de refeições.

## 📋 Descrição

Este diretório contém workflows n8n que implementam o pipeline de IA:
1. Recebe áudio ou texto via webhook
2. Transcreve usando Whisper (OpenAI)
3. Parseia com GPT-4 para JSON estruturado
4. Envia para dispositivo Android via Join API

## 📦 Arquivos

- **fluxo_refeicao.json** - Workflow completo de processamento

## 🔄 Fluxo do Workflow

```mermaid
graph LR
    A[Webhook POST] --> B[Whisper Transcrição]
    B --> C[GPT-4 Parser]
    C --> D[Processar JSON]
    D --> E[Enviar Join]
    E --> F[Resposta Webhook]
```

### Nós do Workflow

#### 1. Webhook - Entrada de Voz
- **Tipo**: Trigger
- **Método**: POST
- **Path**: `/voz-refeicao`
- **Body esperado**:
```json
{
  "audio": "base64_encoded_audio ou texto transcrito",
  "timestamp": "unix_timestamp"
}
```

#### 2. Whisper - Transcrição
- **API**: OpenAI Whisper API
- **Model**: whisper-1
- **Language**: pt (Português)
- **Input**: Audio file ou base64
- **Output**: Texto transcrito

#### 3. GPT - Parser JSON
- **API**: OpenAI GPT-4
- **Temperature**: 0.3 (mais determinístico)
- **System Prompt**: Extrai alimentos, quantidades e tipo de refeição
- **Output**: JSON estruturado

#### 4. Processar JSON
- **Tipo**: Code (JavaScript)
- **Função**: Limpa e valida JSON do GPT
- **Features**:
  - Remove markdown code blocks
  - Adiciona timestamp se ausente
  - Trata erros de parsing

#### 5. Enviar para Join
- **API**: Join Push API
- **Method**: GET com query params
- **Payload**: JSON da refeição
- **Target**: Dispositivo Android específico

#### 6. Resposta Webhook
- **Tipo**: Respond to Webhook
- **Status**: 200 OK
- **Body**: Confirmação de envio

## 🚀 Instalação

### 1. Importar Workflow

```bash
# No n8n
1. Menu → Import from File
2. Selecione: fluxo_refeicao.json
3. Clique em Import
```

### 2. Configurar Credenciais

#### OpenAI
```bash
# No nó "Whisper - Transcrição" e "GPT - Parser JSON"
1. Clique em Credentials
2. Add new → OpenAI
3. Cole API Key: sk-xxxxxxxxxxxxxxxx
4. Save
```

#### Variáveis de Ambiente
```bash
# No servidor n8n
export OPENAI_API_KEY="sk-xxxxxxxxxxxxxxxx"
export JOIN_API_KEY="xxxxxxxxxxxxxxxx"
export JOIN_DEVICE_ID="seu_dispositivo"
```

### 3. Ativar Workflow

```bash
1. No workflow, clique em "Inactive"
2. Mude para "Active"
3. Copie a URL do webhook
```

## 🧪 Testar Workflow

### Teste 1: Com curl (texto direto)

```bash
curl -X POST https://seu-n8n.com/webhook/voz-refeicao \
  -H "Content-Type: application/json" \
  -d '{
    "audio": "Anotei 150 gramas de frango grelhado e 100 gramas de arroz integral",
    "timestamp": "'$(date +%s)'"
  }'
```

### Teste 2: Com audio base64

```bash
# Converter audio para base64
AUDIO_BASE64=$(base64 -w 0 audio.mp3)

curl -X POST https://seu-n8n.com/webhook/voz-refeicao \
  -H "Content-Type: application/json" \
  -d "{
    \"audio\": \"$AUDIO_BASE64\",
    \"timestamp\": \"$(date +%s)\"
  }"
```

### Teste 3: Via Postman

```json
POST https://seu-n8n.com/webhook/voz-refeicao

Headers:
Content-Type: application/json

Body:
{
  "audio": "Registrar jantar com 200g de salmão e salada",
  "timestamp": "1698764400"
}
```

## 📊 Monitoramento

### Ver Execuções

```bash
# No n8n
1. Workflow → Executions
2. Veja histórico de execuções
3. Status: Success (verde) ou Error (vermelho)
```

### Debug de Nó

```bash
1. Clique em qualquer nó
2. "View execution data"
3. Veja Input e Output do nó
```

### Logs

```bash
# Logs do n8n (Docker)
docker logs -f n8n

# Filtrar erros
docker logs n8n 2>&1 | grep ERROR
```

## 🔧 Customização

### Ajustar Prompt do GPT

Edite o nó "GPT - Parser JSON" → Body:

```javascript
{
  "model": "gpt-4",
  "messages": [
    {
      "role": "system",
      "content": "SEU PROMPT PERSONALIZADO AQUI. Exemplo: Você é um nutricionista expert que extrai dados de refeições..."
    }
  ]
}
```

### Adicionar Validação

Adicione um nó "IF" após "Processar JSON":

```javascript
// Validar se tem pelo menos 1 item
{{ $json.itens.length > 0 }}
```

### Salvar em Banco de Dados

Adicione nó após "Processar JSON":
- **MySQL**: Insert refeição
- **MongoDB**: Insert document
- **Google Sheets**: Append row

### Adicionar Notificação Slack

```bash
1. Adicione nó "Slack"
2. Conecte após "Processar JSON"
3. Configure mensagem:
   "Nova refeição cadastrada: {{ $json.refeicao }}"
```

## 💰 Custos Estimados (OpenAI)

| Recurso | Custo por uso | Exemplo |
|---------|---------------|---------|
| Whisper | $0.006/min | 10s de áudio = $0.001 |
| GPT-4 | $0.03/1k tokens | Parse = ~200 tokens = $0.006 |
| **Total por refeição** | **~$0.007** | |

### Otimizar Custos

1. **Use Whisper local** (grátis, mas requer servidor):
```bash
# Whisper.cpp
git clone https://github.com/ggerganov/whisper.cpp
./whisper -m models/ggml-base.bin -l pt audio.wav
```

2. **Use GPT-3.5-turbo** ao invés de GPT-4:
```json
{
  "model": "gpt-3.5-turbo",  // $0.002/1k tokens (15x mais barato)
  ...
}
```

3. **Cache de resultados comuns**:
- Adicione nó para verificar cache antes do GPT
- Armazene combinações frequentes (ex: "frango + arroz")

## 🐛 Troubleshooting

### Erro: "Invalid API Key"

```bash
# Verificar variável de ambiente
echo $OPENAI_API_KEY

# Recarregar n8n
docker restart n8n
```

### Erro: "Webhook not found"

```bash
# Verificar se workflow está ativo
# Verificar URL do webhook (deve ter /webhook/ no path)
# Testar com curl simples
curl https://seu-n8n.com/webhook-test/voz-refeicao
```

### Erro: "Join push failed"

```bash
# Testar Join API diretamente
curl "https://joinjoaomgcd.appspot.com/_ah/api/messaging/v1/sendPush?apikey=YOUR_KEY&deviceId=DEVICE_ID&text=teste"
```

### GPT retorna JSON inválido

```javascript
// Adicione tratamento no nó "Processar JSON"
try {
  // Remove backticks e markdown
  const cleaned = content.replace(/```json\n?/g, '').replace(/```/g, '').trim();
  return JSON.parse(cleaned);
} catch (e) {
  // Tenta extrair JSON com regex
  const match = content.match(/\{[\s\S]*\}/);
  return JSON.parse(match[0]);
}
```

## 🔐 Segurança

### Proteger Webhook

Adicione autenticação no webhook:

```javascript
// Nó IF após Webhook
{{ $json.headers.authorization === "Bearer SEU_TOKEN_SECRETO" }}
```

### Rate Limiting

```javascript
// Adicione nó "Rate Limit"
// Limite: 10 requests por minuto
```

### HTTPS

```bash
# Use sempre HTTPS em produção
# Configure SSL no n8n:
docker run -d \
  -e N8N_PROTOCOL=https \
  -e N8N_SSL_KEY=/certs/privkey.pem \
  -e N8N_SSL_CERT=/certs/fullchain.pem \
  ...
```

## 📚 Recursos

- [n8n Documentation](https://docs.n8n.io/)
- [OpenAI Whisper API](https://platform.openai.com/docs/guides/speech-to-text)
- [OpenAI GPT-4 API](https://platform.openai.com/docs/guides/gpt)
- [Join API Docs](https://joaoapps.com/join/api/)

## 🚀 Próximos Passos

- [ ] Adicionar suporte a múltiplos idiomas
- [ ] Implementar cache de alimentos frequentes
- [ ] Adicionar validação nutricional básica
- [ ] Integrar com Google Calendar (registrar horários)
- [ ] Criar dashboard de analytics
