# 📱 Tasker Profiles

Perfis e tasks do Tasker para captura de voz e automação de preenchimento.

## 📋 Descrição

Este diretório contém perfis XML do Tasker que implementam:
1. Gatilhos por comando de voz
2. Transcrição e envio para n8n
3. Recepção de JSON via Join
4. Automação de preenchimento no Tecnonutri

## 📦 Arquivos

- **voz_refeicao.prf.xml** - Perfil completo com todos os profiles e tasks

## 🏗️ Estrutura do Perfil

### Profile 1: Comando de Voz - Refeição

**Gatilhos (Voice Commands):**
- "anotei*"
- "registrar*"
- "lançar refeição*"

**Task Associada:** Transcrever e Processar Refeição

### Task 2: Transcrever e Processar Refeição

**Ações:**
1. **Reconhecimento de Voz**
   - Idioma: pt-BR
   - Armazena em: %VOICE

2. **Armazenar Transcrição**
   - %transcricao = %VOICE

3. **Feedback Visual**
   - Say: "Processando sua refeição..."

4. **Configurar Webhook**
   - %webhook_url = URL do n8n

5. **HTTP POST**
   - URL: %webhook_url
   - Content-Type: application/json
   - Body: {"audio": "%transcricao", "timestamp": "%TIMES"}
   - Timeout: 30s

6. **Verificar Resposta**
   - IF %HTTPR = 200
   - Say: "Refeição enviada com sucesso!"
   - ELSE
   - Say: "Erro ao processar. Tente novamente."

### Profile 4: Join - Receber Refeição

**Gatilho:** Notificação do Join

**Task Associada:** Preencher Tecnonutri (Join Trigger)

### Task 3: Preencher Tecnonutri (Join Trigger)

**Ações:**
1. **Abrir Tecnonutri**
   - Launch App: br.com.tecnonutri

2. **Aguardar App Abrir**
   - Wait: 2 segundos

3. **Receber JSON**
   - %refeicao_json = %ajcomm (Join command)

4. **Parsear JSON**
   - Parse JSON: %refeicao_json → %refeicao_data

5. **Extrair Dados**
   - %tipo_refeicao = %refeicao_data.refeicao
   - %itens = %refeicao_data.itens

6. **Clicar Adicionar**
   - AutoInput Click: FAB button

7. **Loop por Itens**
   - FOR %item IN %itens():
     - Buscar alimento: %item.alimento
     - Selecionar primeiro resultado
     - Preencher quantidade: %item.quantidade
     - Salvar
     - Wait: 1 segundo

8. **Confirmação**
   - Say: "Refeição cadastrada com sucesso no Tecnonutri!"

## 🚀 Instalação

### Pré-requisitos

1. **Apps necessários:**
   - Tasker (pago)
   - AutoInput (pago)
   - Join (grátis)

2. **Permissões:**
   - Microfone
   - Acessibilidade (para AutoInput)
   - Executar em segundo plano

### Importar Perfil

```bash
# No Android:
1. Abra o Tasker
2. Menu (⋮) → Data → Import Project
3. Navegue até: tasker-profiles/voz_refeicao.prf.xml
4. Selecione o arquivo
5. Confirme a importação
```

### Configurar

#### 1. Atualizar URL do Webhook

```bash
# Task "Transcrever e Processar Refeição" → Ação 4
1. Toque na ação "Variable Set"
2. Altere %webhook_url
3. Substitua: https://seu-n8n.com/webhook/voz-refeicao
```

⚠️ **IMPORTANTE**: O perfil XML contém URL placeholder "https://seu-n8n.com/webhook/voz-refeicao" (linha 71). Este DEVE ser substituído pela URL real do seu n8n antes de usar, caso contrário as requisições falharão. Considere adicionar validação no Tasker para verificar se a URL foi atualizada.

#### 2. Ajustar IDs do Tecnonutri (se necessário)

```bash
# Task "Preencher Tecnonutri" → Ações de AutoInput
# Se o app mudou, atualize os IDs:
- com.android.systemui:id/fab → Novo ID do botão
- br.com.tecnonutri:id/quantity → Novo ID do campo quantidade
```

#### 3. Configurar Join

```bash
1. Abra o app Join
2. Entre com sua conta Google
3. Anote o Device ID
4. Configure no n8n (variável JOIN_DEVICE_ID)
```

## 🧪 Testar Perfil

### Teste 1: Reconhecimento de Voz

```bash
1. Fale: "Anotei 100 gramas de frango"
2. Deve aparecer: "Processando sua refeição..."
3. Verifique os logs do Tasker
```

### Teste 2: HTTP Post

```bash
# Verificar logs do Tasker
1. Tasker → Menu → More → Run Log
2. Procure por "HTTP Post"
3. Verifique variável %HTTPR (deve ser 200)
```

### Teste 3: Preenchimento Completo

```bash
1. Configure n8n e Join corretamente
2. Fale: "Anotei 150g frango grelhado e 100g arroz"
3. Aguarde processamento (5-10 segundos)
4. Tecnonutri deve abrir automaticamente
5. Alimentos devem ser cadastrados
```

## 🔧 Customização

### Adicionar Novos Comandos de Voz

```xml
<!-- No Profile "Comando de Voz - Refeição" -->
<Event sr="con3" ve="2">
    <code>11</code>
    <Str sr="arg0" ve="3">cadastrar café da manhã*</Str>
</Event>
```

### Adicionar Tipo de Refeição no POST

```bash
# Task "Transcrever e Processar" → Ação HTTP Post
# Adicionar ao body:
{
  "audio": "%transcricao",
  "timestamp": "%TIMES",
  "tipo_refeicao": "almoco"  # ou detectar do texto
}
```

### Adicionar Confirmação Visual

```bash
# Antes do HTTP Post, adicione:
1. Alert → "Enviar: %transcricao?"
2. IF %BUTTON = "OK"
3. Continue...
```

### Log de Refeições Local

```bash
# Após receber JSON, adicione:
1. Write File
2. File: /sdcard/Tasker/refeicoes.log
3. Text: %refeicao_json
4. Append: Yes
```

## 📊 Variáveis Disponíveis

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| %VOICE | Texto reconhecido pela voz | "anotei 100g frango" |
| %transcricao | Cópia de %VOICE para processamento | "anotei 100g frango" |
| %webhook_url | URL do webhook n8n | https://n8n.com/webhook/... |
| %HTTPR | Status HTTP da resposta | 200, 404, 500 |
| %refeicao_json | JSON recebido do Join | {"refeicao": "almoco", ...} |
| %refeicao_data | JSON parseado | Objeto com campos |
| %tipo_refeicao | Tipo da refeição | "almoco", "jantar" |
| %itens | Array de itens | [{"alimento": "frango", ...}] |

## 🐛 Troubleshooting

### Perfil não ativa com voz

**Solução:**
```bash
1. Verifique se profile está ATIVO (verde)
2. Teste o reconhecimento: "Ok Google, abrir Tasker"
3. Adicione mais gatilhos variantes:
   - "anotei*"
   - "anotar*"
   - "registrei*"
```

### AutoInput não preenche campos

**Solução:**
```bash
1. Configurações → Acessibilidade → AutoInput → Ativar
2. Tasker → Preferences → Monitor → Display Off Monitoring: Yes
3. Teste manual: Tasker → Tasks → "Preencher Tecnonutri" → Play
```

### HTTP retorna erro

**Solução:**
```bash
# Verificar resposta
1. Adicione após HTTP Post:
   Flash: "Status: %HTTPR - Response: %HTTPD"
2. Se 404: Verifique URL do webhook
3. Se 500: Verifique logs do n8n
```

### Join não dispara task

**Solução:**
```bash
1. Verifique profile "Join - Receber Refeição" está ativo
2. Teste manualmente:
   - Join app → Send Push to This Device
   - Text: teste
   - Deve disparar a task
3. Verifique filtro do event (deve ser "join")
```

## 🔐 Segurança

### Proteger API Key

```bash
# Não coloque API keys direto no XML
# Use variáveis globais:
1. Tasker → Vars → Add
2. Nome: %WEBHOOK_TOKEN
3. Valor: seu_token_secreto
4. Use: Authorization: Bearer %WEBHOOK_TOKEN
```

### Desabilitar em Locais Específicos

```bash
# Adicione context ao Profile
1. Profile → Add → State → Location
2. Selecione casa/trabalho
3. Invert: Yes (só funciona fora desses locais)
```

## 📈 Métricas e Analytics

### Contar Refeições por Dia

```bash
# Adicione ao final da Task 3:
1. Variable Add: %contador_refeicoes + 1
2. Write File: /sdcard/Tasker/stats.txt
3. Text: %DATE %TIME %tipo_refeicao
```

### Notificação Resumo Diário

```bash
# Novo Profile: Time → 23:59
# Task:
1. Read File: /sdcard/Tasker/stats.txt → %stats
2. Notify: "Hoje você registrou X refeições"
```

## 🚀 Próximos Passos

- [ ] Adicionar suporte a fotos de refeições
- [ ] Integrar com Google Fit para calorias
- [ ] Adicionar confirmação visual antes de enviar
- [ ] Criar widget para acesso rápido
- [ ] Implementar modo offline (cache local)

## 📚 Recursos

- [Tasker User Guide](https://tasker.joaoapps.com/userguide/en/)
- [AutoInput Actions](https://joaoapps.com/autoinput/actions/)
- [Join API Documentation](https://joaoapps.com/join/api/)
- [Tasker XML Reference](https://tasker.joaoapps.com/userguide/en/profile_xml.html)

## 💡 Dicas

### Performance
- Use "Wait Until" ao invés de "Wait" fixo
- Desabilite profiles quando não estiver usando
- Limpe variáveis após uso (%transcricao, etc)

### Confiabilidade
- Adicione retry em HTTP requests
- Use timeouts generosos (30s+)
- Implemente fallback local se n8n falhar

### UX
- Use vibração como feedback (Vibrate Pattern)
- Adicione sons customizados (Play Ringtone)
- Mostre preview antes de enviar (Alert)
