# 📱 Guia de Instalação e Configuração

Guia completo para configurar o Assistente de Cadastro de Refeições com IA.

## 📋 Índice

1. [Pré-requisitos](#pré-requisitos)
2. [Configuração do Android](#configuração-do-android)
3. [Configuração do n8n](#configuração-do-n8n)
4. [Configuração das APIs](#configuração-das-apis)
5. [Testes](#testes)
6. [Troubleshooting](#troubleshooting)

---

## 🔧 Pré-requisitos

### Hardware
- Dispositivo Android 7.0+ (API 24+)
- Mínimo 2GB RAM
- Conexão com internet

### Software
- Tasker (app pago - https://tasker.joaoapps.com/)
- AutoInput Plugin (app pago - https://joaoapps.com/autoinput/)
- Join by joaoapps (gratuito - https://joaoapps.com/join/)
- App Tecnonutri instalado

### Serviços Cloud
- Conta OpenAI (para Whisper e GPT)
- Instância n8n (self-hosted ou cloud)
- Conta Join (para notificações push)

---

## 📱 Configuração do Android

### 1. Instalar Apps Necessários

```bash
# Instale via Google Play:
1. Tasker
2. AutoInput
3. Join
4. Tecnonutri
```

### 2. Configurar Permissões

#### Tasker
1. Abra Configurações → Apps → Tasker
2. Permissões → Ative todas:
   - Microfone
   - Armazenamento
   - Sobreposição de tela
   - Executar em segundo plano

#### AutoInput
1. Configurações → Acessibilidade
2. Procure "AutoInput"
3. Ative o serviço
4. Conceda todas as permissões solicitadas

#### Join
1. Abra o app Join
2. Faça login com sua conta Google
3. Anote o **Device ID** (será necessário depois)

### 3. Importar Perfil do Tasker

1. Abra o Tasker
2. Menu → Importar Perfil
3. Selecione o arquivo: `tasker-profiles/voz_refeicao.prf.xml`
4. Confirme a importação

### 4. Configurar Variáveis do Tasker

Edite a Task "Transcrever e Processar Refeição":

1. Ação 4 (Webhook URL):
   - Substitua `https://seu-n8n.com/webhook/voz-refeicao` pela URL real do seu n8n

2. Teste o perfil:
   - Diga "Anotei 100g de frango" para ativar o perfil
   - Verifique se a transcrição está funcionando

---

## ☁️ Configuração do n8n

### 1. Deploy do n8n

**Opção A: Docker (Local ou VPS)**

```bash
docker run -it --rm \
  --name n8n \
  -p 5678:5678 \
  -v ~/.n8n:/home/node/.n8n \
  docker.n8n.io/n8nio/n8n
```

**Opção B: n8n Cloud**
- Acesse https://n8n.io/cloud
- Crie uma conta e workspace

### 2. Importar Workflow

1. Acesse sua instância n8n (http://localhost:5678)
2. Menu → Import from File
3. Selecione: `n8n-flows/fluxo_refeicao.json`
4. Clique em "Import"

### 3. Configurar Credenciais

#### OpenAI API
1. No workflow, clique no nó "Whisper - Transcrição"
2. Credentials → Add new
3. API Key: Cole sua chave da OpenAI
4. Repita para o nó "GPT - Parser JSON"

#### Variáveis de Ambiente
No servidor n8n, adicione:

```bash
export OPENAI_API_KEY="sk-xxxxxxxxxxxxxxxx"
export JOIN_API_KEY="xxxxxxxxxxxxxxxx"
export JOIN_DEVICE_ID="seu_dispositivo_android"
```

### 4. Ativar Workflow

1. No canto superior direito, mude de "Inactive" para **Active**
2. Anote a URL do webhook (algo como: `https://seu-n8n.com/webhook/voz-refeicao`)
3. Esta URL será usada no Tasker

---

## 🔑 Configuração das APIs

### OpenAI API

1. Acesse: https://platform.openai.com/api-keys
2. Crie uma nova API Key
3. Copie a chave (começa com `sk-`)
4. **Importante**: Mantenha créditos na conta (Whisper custa ~$0.006/min)

### Join API

1. Acesse: https://joinjoaomgcd.appspot.com/
2. Faça login com Google
3. Vá em "Join API"
4. Copie sua API Key
5. No app Join (Android), anote o Device ID

### Configurar Arquivo .env

1. Copie o arquivo de exemplo:
```bash
cp config/.env.example config/.env
```

2. Edite `config/.env` com suas credenciais:
```bash
OPENAI_API_KEY=sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
JOIN_API_KEY=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
JOIN_DEVICE_ID=seu_dispositivo_android
N8N_WEBHOOK_URL=https://seu-n8n.com/webhook/voz-refeicao
```

---

## 🧪 Testes

### Teste 1: Transcrição de Voz

1. No Android, diga: **"Anotei 150g de frango grelhado"**
2. O Tasker deve capturar a fala
3. Você verá uma notificação "Processando sua refeição..."

### Teste 2: Pipeline n8n

1. Acesse o n8n
2. Workflow → Executions
3. Verifique se há uma execução recente
4. Deve ter status "Success" (verde)

### Teste 3: Recebimento no Android

1. O Join deve enviar uma notificação
2. Toque na notificação
3. O Tecnonutri deve abrir automaticamente
4. Os alimentos serão cadastrados

### Teste 4: Cadastro Completo

**Comando de voz completo:**
> "Anotei 200g frango grelhado, 150g arroz integral e salada verde"

**Resultado esperado:**
1. ✅ Voz transcrita
2. ✅ JSON gerado pela IA
3. ✅ Notificação recebida
4. ✅ Tecnonutri abre
5. ✅ 3 alimentos cadastrados
6. ✅ Confirmação por voz

---

## 🐛 Troubleshooting

### Problema: Tasker não captura a voz

**Solução:**
- Verifique permissão de microfone em Configurações → Apps → Tasker
- Teste o reconhecimento de voz do Google (diga "Ok Google")
- Reinicie o Tasker

### Problema: Webhook não responde (n8n)

**Solução:**
- Verifique se o workflow está **Active**
- Teste o webhook manualmente com curl:
```bash
curl -X POST https://seu-n8n.com/webhook/voz-refeicao \
  -H "Content-Type: application/json" \
  -d '{"audio": "teste de audio", "timestamp": "1698764400"}'
```
- Verifique os logs do n8n

### Problema: Join não envia notificação

**Solução:**
- Confirme que JOIN_API_KEY e JOIN_DEVICE_ID estão corretos
- Teste manualmente em: https://joinjoaomgcd.appspot.com/
- Verifique se o dispositivo está online no painel Join

### Problema: AutoInput não preenche o Tecnonutri

**Solução:**
- Vá em Configurações → Acessibilidade → AutoInput → Ative
- Teste manualmente: Tasker → Tasks → "Preencher Tecnonutri"
- Verifique se os IDs dos elementos do Tecnonutri não mudaram (atualização do app)

### Problema: GPT não gera JSON correto

**Solução:**
- Teste com comandos mais claros: "Anotei 100 gramas de frango"
- Verifique créditos na conta OpenAI
- Ajuste o prompt do GPT no n8n (nó "GPT - Parser JSON")

### Problema: Credenciais OpenAI inválidas

**Solução:**
- Verifique se a chave API não expirou em https://platform.openai.com/api-keys
- Confirme que há créditos na conta
- Re-gere a chave se necessário

---

## 📊 Monitoramento

### Logs do n8n
- Acesse: Workflow → Executions
- Veja detalhes de cada execução
- Verifique erros nos nós

### Logs do Tasker
- Tasker → Menu → More → Run Log
- Filtre por "Comando de Voz - Refeição"

### Verificar custos OpenAI
- https://platform.openai.com/usage
- Whisper: ~$0.006 por minuto de áudio
- GPT-4: ~$0.03 por 1k tokens

---

## 🚀 Próximos Passos

Após a configuração básica funcionar:

1. **Treinar comandos personalizados**
   - Adicione novos gatilhos no perfil Tasker
   - Exemplos: "registrar jantar", "lançar café da manhã"

2. **Melhorar o prompt GPT**
   - Ajuste para reconhecer seus alimentos favoritos
   - Adicione contexto sobre suas refeições típicas

3. **Adicionar validação visual**
   - Implemente confirmação antes de salvar
   - Mostre preview dos itens identificados

4. **Expandir para outros apps**
   - Adapte para MyFitnessPal, FatSecret, etc.
   - Crie perfis específicos por app

---

## 📚 Recursos Adicionais

- [Documentação Tasker](https://tasker.joaoapps.com/userguide/en/)
- [Documentação n8n](https://docs.n8n.io/)
- [OpenAI API Reference](https://platform.openai.com/docs/api-reference)
- [Join API Docs](https://joaoapps.com/join/api/)

---

## 📞 Suporte

Para dúvidas ou problemas:
- Abra uma issue no GitHub
- Consulte o README.md principal
- Verifique a seção de troubleshooting acima
