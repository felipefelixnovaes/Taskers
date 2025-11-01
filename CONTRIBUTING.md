# 🤝 Contribuindo para o Projeto Taskers

Obrigado por considerar contribuir com o projeto! Este guia vai ajudá-lo a fazer contribuições de qualidade.

## 📋 Índice

1. [Como Contribuir](#como-contribuir)
2. [Reportar Bugs](#reportar-bugs)
3. [Sugerir Melhorias](#sugerir-melhorias)
4. [Desenvolver Novas Features](#desenvolver-novas-features)
5. [Padrões de Código](#padrões-de-código)
6. [Processo de Pull Request](#processo-de-pull-request)

---

## 🚀 Como Contribuir

Existem várias formas de contribuir:

- 🐛 Reportar bugs
- 💡 Sugerir novas features
- 📖 Melhorar documentação
- 🔧 Implementar correções
- ✨ Desenvolver novas funcionalidades
- 🧪 Adicionar testes
- 🌐 Traduzir para outros idiomas

---

## 🐛 Reportar Bugs

### Antes de Reportar

1. Verifique se o bug já foi reportado nas [Issues](https://github.com/felipefelixnovaes/Taskers/issues)
2. Confirme que você está usando a versão mais recente
3. Tente reproduzir o bug em diferentes condições

### Como Reportar

Crie uma nova issue com:

```markdown
**Descrição do Bug**
Descrição clara e concisa do problema.

**Como Reproduzir**
1. Vá para '...'
2. Execute '...'
3. Observe '...'

**Comportamento Esperado**
O que deveria acontecer.

**Comportamento Atual**
O que está acontecendo.

**Screenshots/Logs**
Se aplicável, adicione capturas de tela ou logs.

**Ambiente:**
- Dispositivo: [ex: Samsung Galaxy S21]
- Android: [ex: 13]
- Tasker: [ex: 6.2.20]
- n8n: [ex: 1.15.0]

**Informações Adicionais**
Qualquer outra informação relevante.
```

---

## 💡 Sugerir Melhorias

### Template para Sugestões

```markdown
**Descrição da Feature**
Descrição clara do que você gostaria de adicionar.

**Problema que Resolve**
Qual problema esta feature resolve?

**Solução Proposta**
Como você imagina que funcione?

**Alternativas Consideradas**
Que outras soluções você pensou?

**Impacto**
- [ ] Melhora performance
- [ ] Adiciona nova funcionalidade
- [ ] Melhora UX
- [ ] Melhora segurança
```

---

## 🔧 Desenvolver Novas Features

### 1. Setup do Ambiente

```bash
# Clone o repositório
git clone https://github.com/felipefelixnovaes/Taskers.git
cd Taskers

# Crie uma branch para sua feature
git checkout -b feature/nome-da-feature
```

### 2. Áreas do Projeto

#### n8n Workflows
```bash
# Localização: n8n-flows/
# Para modificar:
1. Importe o workflow no n8n
2. Faça suas alterações
3. Exporte o workflow
4. Substitua o arquivo JSON
```

#### Tasker Profiles
```bash
# Localização: tasker-profiles/
# Para modificar:
1. Importe no Tasker
2. Faça alterações
3. Exporte como XML
4. Substitua o arquivo
```

#### Android Service
```bash
# Localização: android-service/
# Linguagem: Kotlin
# Para modificar:
1. Abra no Android Studio
2. Implemente mudanças
3. Teste no emulador/dispositivo
4. Commit o código
```

### 3. Teste Suas Mudanças

```bash
# n8n
- Teste manualmente com curl
- Verifique logs de execução
- Teste casos de erro

# Tasker
- Execute tasks manualmente
- Teste gatilhos de voz
- Verifique logs do Tasker

# Android Service
- Use adb logcat
- Teste em diferentes versões do Android
- Teste com diferentes versões do Tecnonutri
```

---

## 📝 Padrões de Código

### n8n Workflows

```json
{
  "nodes": [
    {
      "name": "Nome Descritivo do Nó",
      "parameters": {
        // Parâmetros organizados e comentados
      }
    }
  ]
}
```

**Diretrizes:**
- Use nomes descritivos para nós
- Adicione comentários em nós complexos
- Mantenha fluxo linear e legível
- Trate erros adequadamente

### Kotlin (Android Service)

```kotlin
/**
 * Documentação da função
 * @param parametro Descrição do parâmetro
 * @return Descrição do retorno
 */
fun minhaFuncao(parametro: String): Boolean {
    // Implementação
    return true
}
```

**Diretrizes:**
- Use KDoc para documentação
- Siga [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use nomes significativos
- Evite funções muito longas
- Adicione logs informativos

### XML (Tasker)

```xml
<!-- Comentário descrevendo a ação -->
<Action sr="act1" ve="7">
    <code>339</code>
    <Str sr="arg0" ve="3">%variavel_descritiva</Str>
</Action>
```

**Diretrizes:**
- Use nomes descritivos para variáveis
- Adicione comentários em ações complexas
- Organize ações logicamente
- Use delays apropriados

### Documentação (Markdown)

```markdown
# Título Principal

Descrição breve.

## Seção

Conteúdo detalhado.

### Subseção

- Item 1
- Item 2

\`\`\`bash
# Exemplo de código
comando exemplo
\`\`\`
```

**Diretrizes:**
- Use títulos hierárquicos
- Adicione exemplos práticos
- Inclua capturas de tela quando apropriado
- Mantenha linguagem clara e objetiva

---

## 🔄 Processo de Pull Request

### 1. Antes de Submeter

- [ ] Código está funcionando localmente
- [ ] Testes foram executados
- [ ] Documentação foi atualizada
- [ ] Commits seguem padrão (ver abaixo)
- [ ] Não há conflitos com main

### 2. Padrão de Commits

Use [Conventional Commits](https://www.conventionalcommits.org/):

```bash
# Features
git commit -m "feat: adiciona suporte a múltiplos idiomas"

# Correções
git commit -m "fix: corrige parsing de JSON inválido"

# Documentação
git commit -m "docs: atualiza guia de instalação"

# Refatoração
git commit -m "refactor: reorganiza estrutura do workflow n8n"

# Testes
git commit -m "test: adiciona testes para parser de voz"

# Outros
git commit -m "chore: atualiza dependências"
```

### 3. Criar Pull Request

```markdown
**Descrição**
Descrição clara do que foi alterado e por quê.

**Tipo de Mudança**
- [ ] Bug fix
- [ ] Nova feature
- [ ] Breaking change
- [ ] Documentação

**Checklist**
- [ ] Código testado localmente
- [ ] Documentação atualizada
- [ ] Sem conflitos com main
- [ ] Commits seguem padrão

**Screenshots**
Se aplicável, adicione capturas de tela.

**Issues Relacionadas**
Closes #123
```

### 4. Revisão

- Responda aos comentários de revisão
- Faça ajustes solicitados
- Aguarde aprovação de um maintainer
- Após aprovação, seu PR será merged!

---

## 🎨 Áreas Que Precisam de Ajuda

### Alta Prioridade

- [ ] Suporte a outros apps de nutrição (MyFitnessPal, FatSecret)
- [ ] Implementação de testes automatizados
- [ ] Melhorar tratamento de erros
- [ ] Adicionar modo offline

### Média Prioridade

- [ ] Interface visual para confirmação
- [ ] Dashboard de analytics
- [ ] Integração com Google Fit
- [ ] Suporte a fotos de refeições

### Baixa Prioridade

- [ ] Tradução para inglês/espanhol
- [ ] Tema escuro na documentação
- [ ] Melhorias de performance
- [ ] Otimização de custos (API)

---

## 📚 Recursos Úteis

### Documentação Oficial

- [Tasker User Guide](https://tasker.joaoapps.com/userguide/en/)
- [n8n Documentation](https://docs.n8n.io/)
- [Android Accessibility](https://developer.android.com/guide/topics/ui/accessibility)
- [OpenAI API](https://platform.openai.com/docs)

### Comunidade

- [Discussões no GitHub](https://github.com/felipefelixnovaes/Taskers/discussions)
- [Issues](https://github.com/felipefelixnovaes/Taskers/issues)

---

## 💬 Dúvidas?

Se tiver qualquer dúvida sobre como contribuir:

1. Abra uma [Discussion](https://github.com/felipefelixnovaes/Taskers/discussions)
2. Entre em contato via Issues
3. Consulte a documentação existente

---

## 🙏 Agradecimentos

Obrigado por dedicar seu tempo para melhorar este projeto! Toda contribuição, por menor que seja, é muito valorizada.

---

**Nota**: Este é um projeto open-source mantido por voluntários. Seja paciente e respeitoso em todas as interações.
