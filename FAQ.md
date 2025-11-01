# ❓ FAQ - Perguntas Frequentes

Respostas para as dúvidas mais comuns sobre o Assistente de Refeições com IA.

## 📱 Geral

### O que é este projeto?

Um assistente de voz com IA que automatiza o registro de refeições no app Tecnonutri. Você fala o que comeu, e o sistema interpreta, estrutura os dados e preenche o app automaticamente.

### É grátis?

O código é open-source (grátis), mas alguns componentes têm custos:
- **Tasker**: ~$3.49 (compra única)
- **AutoInput**: ~$2.99 (compra única)
- **OpenAI API**: ~$0.007 por refeição
- **n8n**: Grátis (self-hosted) ou pago (cloud)
- **Join**: Grátis

**Custo médio mensal**: ~$5-10 (dependendo do uso)

### Funciona offline?

Não completamente. Requer internet para:
- Transcrição de voz (Whisper)
- Parsing com IA (GPT-4)
- Comunicação entre n8n e Android

Você pode usar Whisper local para economizar custos de API.

### Quais idiomas são suportados?

Atualmente apenas **Português (pt-BR)**. O sistema pode ser adaptado para outros idiomas modificando:
- Comandos de voz no Tasker
- Prompt do GPT-4
- Idioma do Whisper

### É seguro?

Sim. O projeto:
- ✅ Não armazena dados sensíveis
- ✅ Usa HTTPS para comunicação
- ✅ Não coleta informações pessoais
- ✅ Código open-source auditável
- ⚠️ Requer permissões de acessibilidade (necessário para AutoInput)

---

## 🔧 Instalação e Configuração

### Quanto tempo leva para configurar?

Aproximadamente **15-30 minutos** seguindo o [QUICKSTART.md](./QUICKSTART.md).

### Preciso saber programar?

Não para uso básico. Mas conhecimento técnico ajuda para:
- Debug de problemas
- Customizações avançadas
- Modificar workflows n8n

### Posso usar sem Tasker?

Não na versão atual. O Tasker é essencial para:
- Capturar comandos de voz
- Executar AutoInput
- Receber notificações do Join

**Alternativa**: Você pode desenvolver um app Android standalone usando o código em `android-service/`.

### Funciona em iOS?

Não. O projeto é específico para Android devido ao Tasker e AutoInput. Uma versão iOS requereria desenvolvimento completo de um app nativo.

### Posso usar outro app de nutrição?

Sim, com modificações. Você precisaria:
1. Identificar IDs dos elementos UI do novo app
2. Atualizar código em `TecnonutriFillService.kt` ou perfil Tasker
3. Ajustar delays e fluxo conforme o app

Apps similares: MyFitnessPal, FatSecret, Yazio.

---

## 🎤 Reconhecimento de Voz

### Quais comandos posso usar?

Comandos suportados:
```bash
✅ "Anotei 150g de frango grelhado"
✅ "Registrar almoço com 200g arroz e 100g feijão"
✅ "Lançar refeição 1 pão francês e 1 café"
✅ "Anotei 2 ovos mexidos e 1 fatia de queijo"
```

A IA é flexível e entende variações naturais.

### E se a transcrição estiver errada?

Você pode:
1. **Repetir o comando** - Fale mais devagar e claro
2. **Verificar no n8n** - Veja o que foi transcrito nos logs
3. **Ajustar no Tecnonutri** - Corrija manualmente se necessário
4. **Melhorar o ambiente** - Reduza ruído de fundo

### Posso usar em qualquer idioma?

Tecnicamente sim, mas requer configuração:
1. Mudar `language: "pt"` para outro código no Whisper
2. Adaptar prompt do GPT-4 para o novo idioma
3. Modificar gatilhos do Tasker

### Funciona com sotaques regionais?

Sim! O Whisper da OpenAI é treinado com diversos sotaques. Taxa de acerto geralmente >95%.

---

## 🤖 IA e Processamento

### Qual IA é usada?

- **Whisper** (OpenAI): Transcrição de voz para texto
- **GPT-4** (OpenAI): Parsing e estruturação de dados

Você pode substituir por alternativas:
- Whisper local (grátis)
- GPT-3.5-turbo (mais barato)
- Claude, Gemini, etc. (requer modificação no n8n)

### A IA entende qualquer alimento?

Na maioria dos casos sim, mas funciona melhor com:
- ✅ Alimentos comuns (frango, arroz, feijão)
- ✅ Quantidades claras (150g, 1 xícara)
- ⚠️ Pode confundir alimentos muito específicos ou regionais

**Dica**: Use nomes simples e diretos.

### E se a IA interpretar errado?

A IA tem alta precisão (~85-90%), mas erros acontecem. Você pode:
1. **Verificar JSON** - Inspecione o resultado no n8n
2. **Ajustar prompt** - Melhore as instruções do GPT
3. **Adicionar validação** - Implemente confirmação antes de salvar
4. **Treinar com exemplos** - Adicione few-shot examples no prompt

### Quanto custa usar a IA?

**Por refeição** (~30s áudio + 200 tokens):
- Whisper: $0.001
- GPT-4: $0.006
- **Total**: ~$0.007

**Mensal** (90 refeições):
- ~$0.63 (menos de R$4)

Para reduzir custos:
- Use Whisper local (grátis)
- Use GPT-3.5-turbo ($0.002/1k tokens)

---

## 📱 Android e Tasker

### Por que preciso do AutoInput?

O AutoInput simula toques e digitação no Tecnonutri. É a única forma de preencher o app automaticamente sem modificá-lo.

### O Tasker consome muita bateria?

Não significativamente. O perfil só ativa quando você fala comandos específicos. Impacto estimado: <2% bateria/dia.

### Posso desativar temporariamente?

Sim! No Tasker:
1. Toque no profile "Comando de Voz - Refeição"
2. Toggle para desativar (fica cinza)

### Funciona com tela bloqueada?

Sim, mas requer configuração:
1. Tasker → Preferences → Monitor
2. Ative "Display Off Monitoring"
3. Ative "Run In Foreground"

### O que fazer se o Tasker crashar?

1. Force stop: Configurações → Apps → Tasker → Forçar parada
2. Limpe cache
3. Reinicie o dispositivo
4. Reimporte o perfil se necessário

---

## ☁️ n8n e Infraestrutura

### Preciso ter servidor próprio?

Não necessariamente. Opções:
1. **n8n Cloud** (pago, fácil): https://n8n.io/cloud
2. **Self-hosted** (grátis, requer servidor):
   - VPS (DigitalOcean, AWS, etc.)
   - Raspberry Pi em casa
   - Docker no PC

### Quanto custa hospedar n8n?

- **n8n Cloud**: A partir de $20/mês
- **VPS**: $5-10/mês (DigitalOcean, Hetzner)
- **Self-hosted**: Grátis (custo de energia insignificante)

### Posso compartilhar o n8n com amigos?

Sim! Configure múltiplos dispositivos Join e adicione lógica para identificar usuários.

### E se o n8n cair?

O sistema não funcionará até o n8n voltar. Para alta disponibilidade:
- Use n8n Cloud (SLA 99.9%)
- Configure fallback local no Tasker
- Implemente retry automático

---

## 🔐 Segurança e Privacidade

### Meus dados são armazenados onde?

- **Voz**: Processada pela OpenAI (não armazenada permanentemente)
- **JSON**: Transita pelo n8n (não armazenado)
- **Refeições**: Apenas no Tecnonutri (seu app)

### A OpenAI usa meus dados para treinar IA?

Por padrão, não. Dados da API não são usados para treinamento, mas verifique os [termos de uso da OpenAI](https://openai.com/policies/api-data-usage-policies).

### Como proteger minhas API keys?

1. **Nunca commite** keys no Git
2. **Use variáveis de ambiente** (.env)
3. **Rotacione periodicamente** as keys
4. **Limite permissões** (use keys com escopo mínimo)

### O AutoInput pode ser mal usado?

Sim, por isso requer permissões de acessibilidade. Garanta:
- Baixe apenas da Play Store oficial
- Não compartilhe seu dispositivo
- Revogue permissões se não usar

---

## 🐛 Troubleshooting

### "Invalid API Key"

**Causas**:
- Key expirada ou inválida
- Sem créditos na conta OpenAI
- Typo ao copiar a key

**Solução**:
1. Gere nova key em https://platform.openai.com/api-keys
2. Verifique saldo em https://platform.openai.com/usage
3. Atualize no n8n

### "Webhook not found (404)"

**Causas**:
- URL incorreta no Tasker
- Workflow não está ativo
- n8n está offline

**Solução**:
1. Verifique se workflow está "Active" no n8n
2. Copie URL correta do webhook
3. Atualize variável %webhook_url no Tasker

### "AutoInput não funciona"

**Causas**:
- Serviço de acessibilidade desativado
- IDs dos elementos mudaram (atualização do Tecnonutri)
- Permissões insuficientes

**Solução**:
1. Configurações → Acessibilidade → AutoInput → Ativar
2. Permita "Exibir sobre outros apps"
3. Se Tecnonutri atualizou, atualize IDs no código

### "Join não envia notificação"

**Causas**:
- API key ou Device ID incorreto
- Dispositivo offline
- Firewall bloqueando

**Solução**:
1. Verifique credenciais em https://joinjoaomgcd.appspot.com/
2. Teste conexão: Settings → Network
3. Reinstale o app Join

---

## 💡 Customização

### Como adicionar novos comandos?

Edite o perfil Tasker:
```xml
<Event sr="con3" ve="2">
    <Str sr="arg0" ve="3">meu novo comando*</Str>
</Event>
```

### Como mudar o tipo de refeição padrão?

Ajuste o prompt do GPT-4 no n8n para inferir:
```javascript
"Se mencionado 'café da manhã', use tipo 'cafe'. 
Se 'almoço', use 'almoco'. 
Se não especificado, inferir pelo horário."
```

### Posso adicionar cálculo de calorias?

Sim! Adicione no n8n:
1. Consulte API nutricional (ex: USDA Food API)
2. Calcule total de calorias
3. Inclua no JSON enviado

### Como salvar histórico local?

Adicione no Tasker, após receber JSON:
```
Write File:
  File: /sdcard/Tasker/refeicoes.log
  Text: %refeicao_json
  Append: Yes
```

---

## 📊 Performance

### Quanto tempo demora o processo completo?

Tempo médio: **10-15 segundos**
- Transcrição: 1-3s
- Parsing IA: 0.5-1s
- Join push: 1-2s
- Automação UI: 5-10s

### Como melhorar a velocidade?

1. **Use Whisper local** (remove latência de rede)
2. **Reduza delays** no Tasker (se seu device for rápido)
3. **Otimize n8n** (self-hosted com boa conexão)
4. **Cache resultados** (alimentos frequentes)

### Quantas refeições posso processar por dia?

Ilimitado tecnicamente, mas considere:
- **OpenAI rate limits**: 3,000 requests/min (tier 1)
- **Custos**: ~$0.007 por refeição
- **Bateria**: Impacto mínimo

---

## 🚀 Futuro

### Haverá versão iOS?

Não planejado no momento devido à complexidade. Contribuições são bem-vindas!

### Suporte a outros apps?

Planejado! Prioridades:
1. MyFitnessPal
2. FatSecret
3. Yazio

### Terá interface gráfica?

Sim, está no roadmap (v1.3):
- Confirmação visual antes de salvar
- Editor de refeições
- Dashboard de analytics

### Posso contribuir?

Absolutamente! Veja [CONTRIBUTING.md](./CONTRIBUTING.md) para diretrizes.

---

## 📞 Suporte

### Ainda tenho dúvidas. E agora?

1. **Documentação**: Leia [INSTALL.md](./INSTALL.md) e [README.md](./README.md)
2. **Issues**: Abra uma [issue](https://github.com/felipefelixnovaes/Taskers/issues)
3. **Discussões**: Participe das [discussions](https://github.com/felipefelixnovaes/Taskers/discussions)

### Como reportar bugs?

Veja template em [CONTRIBUTING.md](./CONTRIBUTING.md#reportar-bugs)

### Onde sugerir melhorias?

Abra uma [feature request](https://github.com/felipefelixnovaes/Taskers/issues/new) com template de sugestão.

---

**Última atualização**: 2025-11-01  
**Não encontrou sua resposta?** [Abra uma issue](https://github.com/felipefelixnovaes/Taskers/issues/new) ou [discussão](https://github.com/felipefelixnovaes/Taskers/discussions/new)!
