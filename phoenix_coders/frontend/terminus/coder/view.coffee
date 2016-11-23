Pagination  = require("pokeball").Pagination
Modal = require("pokeball").Modal

updateTemplate = Handlebars.templates["terminus/coder/frontend_templates/update"]

class ModelManage
  constructor: ($)->
    @bindEvent()

  bindEvent: ()->
    $('.date-create-form').on "submit", @addDataUnit
    $('.data-update-form').on "submit", @updateDateUnit
    $('.js-del').on "click", @delDataUnit
    $('.js-edit').on "click", @editDataUnit
    $('.js-update').on "click", @updateDateUnit
    $('.js-clear').on "click", @clearValue

    pageSize = if _.isNumber($.query.get("pageSize")) then $.query.get("pageSize") else 20
    new Pagination(".table-pagination").total($(".table-pagination ").data("total"))
    .show(pageSize, {
      num_display_entries: 5, jump_switch: true, page_size_switch: true, maxPage: -1
    })

  addDataUnit: (evt) =>
    evt.preventDefault()
    $form = $(evt.currentTarget)
    $form.validator({
      identifier: "input.js-need-validated",
      isErrorOnParent: true,
    })

    dataUnit = $form.serializeObject()
    url = "/api/coder"
    $.ajax
      url: url
      type: "POST"
      contentType: "application/json"
      data: JSON.stringify(dataUnit)
      success: (data) =>
        window.location.href = "/terminus"

  delDataUnit: (evt) =>
    $self = $(evt.currentTarget)
    $el = $self.closest("tr")
    url = "/api/coder/delete?coderId=" + $self.data("id")

    $.ajax
      url: url
      type: "POST"
      success: (data) =>
        $el.remove()
        window.location.reload()

  updateDateUnit: (evt) =>
    evt.preventDefault()
    $form = $(evt.currentTarget).closest("form")
    dataUnit = $form.serializeObject()
    dataUnit = Object.assign {}, dataUnit, {id: $(evt.currentTarget).data("id")}
    url = "/api/coder"
    $.ajax
      url: url
      type: "POST"
      contentType: "application/json"
      data: JSON.stringify(dataUnit)
      success: (data) =>
        window.location.reload()

  editDataUnit: (evt) =>
    info = $(evt.currentTarget).closest("tr").data("info")
    new Modal(updateTemplate({data: info})).show()
    $(".data-update-form").on "submit", @updateDateUnit
    $(".data-update-form").validator
      isErrorOnParent: true

  clearValue: (evt) =>
    $(evt.currentTarget).closest("form").find("input").val("")

module.exports = ModelManage
