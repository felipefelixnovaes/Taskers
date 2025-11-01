# 🚀 Quick Start Guide

Comece a usar o Assistente de Refeições com IA em minutos!

## ⚡ Setup Rápido (15 minutos)

### 1️⃣ Pré-requisitos (5 min)

**No Android:**
- [ ] Instalar [Tasker](https://play.google.com/store/apps/details?id=net.dinglisch.android.taskerm)
- [ ] Instalar [AutoInput](https://play.google.com/store/apps/details?id=com.joaomgcd.autoinput)
- [ ] Instalar [Join](https://play.google.com/store/apps/details?id=com.joaomgcd.join)
- [ ] Instalar [Tecnonutri](https://play.google.com/store/apps/details?id=br.com.tecnonutri)

**Credenciais:**
- [ ] Criar conta [OpenAI](https://platform.openai.com/) e gerar API Key
- [ ] Criar conta [n8n Cloud](https://n8n.io/cloud) (ou instalar localmente)
- [ ] Conta Join (usar Google para login)

### 2️⃣ Configurar n8n (5 min)

```bash
# 1. Acessar n8n (cloud ou local)
https://seu-workspace.n8n.cloud

# 2. Importar workflow
Menu → Import from File → Selecione: n8n-flows/fluxo_refeicao.json

# 3. Configurar credenciais
- Clique em "Whisper - Transcrição"
- Add credentials → OpenAI → Cole sua API Key
- Repita para "GPT - Parser JSON"

# 4. Ativar workflow
Toggle "Inactive" → "Active"

# 5. Copiar URL do webhook
Deve ser algo como: https://seu-workspace.n8n.cloud/webhook/voz-refeicao
```

### 3️⃣ Configurar Android (5 min)

**Importar Perfil Tasker:**
```bash
1. Abra Tasker
2. Menu → Import Project
3. Selecione: tasker-profiles/voz_refeicao.prf.xml
4. Confirme importação
```

**Atualizar URL do Webhook:**
```bash
1. Tasker → Tasks → "Transcrever e Processar Refeição"
2. Toque na Ação 4 (Variable Set)
3. Altere %webhook_url para URL do seu n8n
4. Salve
```

**Configurar Join:**
```bash
1. Abra app Join
2. Login com Google
3. Anote o Device ID (ex: "meu_celular")
4. No n8n, adicione variáveis de ambiente:
   - JOIN_API_KEY (da dashboard Join)
   - JOIN_DEVICE_ID (do app)
```

**Ativar Permissões:**
```bash
1. Configurações → Acessibilidade → AutoInput → Ativar
2. Configurações → Apps → Tasker → Permissões → Ativar todas
```

## 🧪 Teste Rápido

### Teste 1: Voz → Webhook
```bash
1. Fale: "Anotei 100 gramas de frango"
2. Deve aparecer: "Processando sua refeição..."
3. ✅ Sucesso se não houver erro
```

### Teste 2: Webhook → IA
```bash
# Teste com curl
curl -X POST https://seu-n8n.cloud/webhook/voz-refeicao \
  -H "Content-Type: application/json" \
  -d '{"audio": "anotei 150g frango grelhado", "timestamp": "1698764400"}'

# ✅ Sucesso se retornar JSON com status 200
```

### Teste 3: End-to-End
```bash
1. Fale: "Anotei 200 gramas de frango grelhado e 100 gramas de arroz integral"
2. Aguarde 5-10 segundos
3. Join deve enviar notificação
4. Tecnonutri deve abrir automaticamente
5. Alimentos devem ser cadastrados
6. ✅ Sucesso se ambos forem adicionados
```

## 🎯 Comandos de Voz Suportados

```bash
✅ "Anotei 150g de frango grelhado"
✅ "Registrar almoço com 200g arroz e 100g feijão"
✅ "Lançar refeição 1 pão francês e 1 café"
✅ "Anotei 2 ovos mexidos e 1 fatia de queijo"
```

## 🐛 Problemas Comuns

### Voz não é capturada
```bash
❌ Problema: Tasker não responde ao comando
✅ Solução: 
   1. Verifique se profile está ATIVO (verde)
   2. Teste "Ok Google" para verificar STT
   3. Configurações → Apps → Tasker → Microfone → Permitir
```

### Webhook retorna erro
```bash
❌ Problema: HTTP 404 ou 500
✅ Solução:
   1. Verifique se workflow está Active no n8n
   2. Copie URL correta do webhook
   3. Teste com curl (comando acima)
```

### AutoInput não preenche
```bash
❌ Problema: Tecnonutri abre mas campos não são preenchidos
✅ Solução:
   1. Configurações → Acessibilidade → AutoInput → Ativar
   2. Permita "Exibir sobre outros apps"
   3. Reinicie o Tasker
```

## 📚 Próximos Passos

Após o setup básico funcionar:

1. **Personalize comandos** - Adicione gatilhos no Tasker
2. **Ajuste o prompt GPT** - Melhore parsing no n8n
3. **Configure backup** - Salve refeições localmente
4. **Analytics** - Acompanhe suas refeições

## 📖 Documentação Completa

- [INSTALL.md](./INSTALL.md) - Guia de instalação detalhado
- [README.md](./README.md) - Visão geral do projeto
- [CONTRIBUTING.md](./CONTRIBUTING.md) - Como contribuir

## 💬 Ajuda

Problemas? Abra uma [issue](https://github.com/felipefelixnovaes/Taskers/issues)!

---

**Tempo total**: ~15 minutos  
**Custo por refeição**: ~$0.007 (OpenAI)  
**Dificuldade**: Intermediário

🎉 **Parabéns!** Você está pronto para automatizar suas refeições!
